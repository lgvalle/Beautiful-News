const ONE_HOUR = 3600000

var functions = require('firebase-functions');
const URL_THE_GUARDIAN = "https://www.theguardian.com/uk/london/rss"
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var Client = require('node-rest-client').Client;
var client = new Client();

exports.fetchGuardianWithoutCache = functions.https.onRequest((req, res) => {
    console.log("Fetching The Guardian Without Cache");
    return request(URL_THE_GUARDIAN)
        .then(data => cleanUp(data))
        .then(items => saveInDatabase(items))
        .then(items => response(res, items, 201))

});

exports.fetchGuardian = functions.https.onRequest((req, res) => {
    console.log("Fetching The Guardian");
    var lastEdition = admin.database().ref('/feed/guardian');
    return lastEdition
        .once('value')
        .then(snapshot => {
            if (isCacheValid(snapshot)) {
                return response(res, snapshot.val(), 200)
            } else {
                return request(URL_THE_GUARDIAN)
                    .then(data => cleanUp(data))
                    .then(items => saveInDatabase(lastEdition, items))
                    .then(items => response(res, items, 201))
            }
        })
});

function saveInDatabase(databaseRef, items) {
    return databaseRef
        .set({
            date: new Date(Date.now()).toISOString(),
            items: items
        })
        .then(() => {
            return Promise.resolve(items);
        })
}

function request(url) {
    return new Promise(function (fulfill, reject) {
        client.get(url, function (data, response) {
            fulfill(data)
        })
    })
}

function response(res, items, code) {
    return Promise.resolve(res.status(code)
        .type('application/json')
        .send(items))
}

function isCacheValid(snapshot) {
    return (snapshot.exists() && elapsed(snapshot.val().date) < ONE_HOUR)
}

function elapsed(date) {
    const then = new Date(date)
    const now = new Date(Date.now())
    return now.getTime() - then.getTime()
}

function cleanUp(data) {
    console.log("Cleaning up data: ", data)
    // Empty array to add clean up elements
    const items = []
    // We are only interested in 'channel' children
    const channel = data.rss.channel

    channel.item.forEach(element => {
        item = {
            title: element.title,
            description: element.description,
            date: element.pubDate,
            creator: element['dc:creator'],
            media: []
        }
        // Iterates through all the elements named '<media:content>' extracting the info we care about
        element['media:content'].forEach(mediaContent => {
            item.media.push({
                url: mediaContent.$.url,                // Parses media:content url attribute
                credit: mediaContent['media:credit']._ // Parses media:cretit tag content
            })
        });
        items.push(item);
    });
    return Promise.resolve(items);
}
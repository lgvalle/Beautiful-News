const ONE_HOUR = 3600000

var functions = require('firebase-functions');
const URL_THE_GUARDIAN = "https://www.theguardian.com/uk/london/rss"
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var Client = require('node-rest-client').Client;
var client = new Client();

const Language = require('@google-cloud/language');
const language = Language();

exports.fetchGuardianWithoutCache = functions.https.onRequest((req, res) => {
    console.log("Fetching The Guardian Without Cache");
    return request(URL_THE_GUARDIAN)
        .then(data => cleanUp(data))
        //.then(items => saveInDatabase(items))
        .then(items => analyze(items))
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
    // We are only interested in 'channel' children
    const channel = data.rss.channel

    const items = channel.item.map(element => {
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

        return analyzeItem(item)

    })
    return Promise.all(items)

    //return Promise.all(items);
}

function analyze(items) {
    const prArray = items.map(item => {
        const document = {
            'content': item.description,
            type: 'PLAIN_TEXT'
        };

        return language.analyzeSentiment({ 'document': document })
            .then((results) => {
                const sentiment = results[0].documentSentiment;
                item.score = sentiment.score
                console.log(`Text: ${item.description}`);
                console.log(`Sentiment score: ${sentiment.score}`);
                console.log(`Item score: ${item.score}`);
                console.log(`Sentiment magnitude: ${sentiment.magnitude}`);
                return item
            })
            .catch((err) => {
                console.error('ERROR:', err);
            })
    })

    return Promise.all(prArray)
}

function analyzeItem(item) {
    const document = {
        'content': item.description,
        type: 'PLAIN_TEXT'
    };

    return language.analyzeSentiment({ 'document': document })
        .then((results) => {
            const sentiment = results[0].documentSentiment;
            item.score = sentiment.score
            item.magnitude = sentiment.magnitude
            return item
        })
        .catch((err) => {
            console.error('ERROR:', err);
        })
}


/*
function analyze(item) {
    // Detects the sentiment of the text
    const document = {
        'content': item.description,
        type: 'PLAIN_TEXT'
    };
    const results = await language.analyzeSentiment({ 'document': document })


    const sentiment = results[0].documentSentiment;

    console.log(`Text: ${item.description}`);
    console.log(`Sentiment score: ${sentiment.score}`);
    console.log(`Sentiment magnitude: ${sentiment.magnitude}`);

    return item
}
*/

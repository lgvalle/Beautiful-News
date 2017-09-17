const ONE_HOUR = 3600000
var functions = require('firebase-functions');

var Client = require('node-rest-client').Client;
var client = new Client();

const admin = require('firebase-admin');

const URL_EL_PAIS = "https://ep00.epimg.net/rss/elpais/portada.xml"
const URL_THE_GUARDIAN = "https://www.theguardian.com/uk/london/rss"

admin.initializeApp(functions.config().firebase);

exports.fetch = functions.https.onRequest((req, res) => {
    console.log("function starting");
    var lastEdition = admin.database().ref('/feed/last');
    return lastEdition
        .once("value")
        .then(snapshot => parse(snapshot, res, lastEdition));
});

exports.fetchGuardian = functions.https.onRequest((req, res) => {
    console.log("Fetching The Guardian");
    var lastEdition = admin.database().ref('/feed/guardian');
    return lastEdition
        .once("value")
        .then(snapshot => parse(snapshot, res, lastEdition));
});



function parse(snapshot, res, lastEdition) {
    if (snapshot.exists() && elapsed(snapshot.val().date) < ONE_HOUR) {
        console.log("Exist & still valid -> return from DB")
        return res.status(200)
            .type('application/json')
            .send(snapshot.val());

    } else {
        console.log("Exist but old -> continue")
    }

    console.log("Missing -> fetch")
    client.get(URL_THE_GUARDIAN, function (data, response) {
        console.log("feed fetched");
        //const items = parseChannel(data.rss.channel)
        const items = cleanUp(data)

        return lastEdition
            .set({
                date: new Date(Date.now()).toISOString(),
                items: items
            })
            .then(function () {
                res.status(201)
                    .type('application/json')
                    .send(items)
            })
    });

}

function elapsed(date) {
    const then = new Date(date)
    const now = new Date(Date.now())
    return now.getTime() - then.getTime()
}

function parseChannel(channel) {
    const items = []
    const promises = []

    channel.item.forEach(element => {
        const item = makeItem(element);

        //promises.push(analyseItem(item));

        items.push(item);
    });

    /*
    Promise.all(promises).then(results => {
        results.forEach(result => {
            const sentiment = result[0].documentSentiment;

            console.log(`Text: ${item.description}`);
            console.log(`Sentiment score: ${sentiment.score}`);
            console.log(`Sentiment magnitude: ${sentiment.magnitude}`);

            item.sentimentScore = sentiment.score;
            item.sentimentMagnitude = sentiment.magnitude;
        })
    })
    */


    return items;
}

function analyseItem(item) {
    const document = {
        'content': item.description,
        type: 'PLAIN_TEXT'
    };

    // Detects the sentiment of the text
    return language.analyzeSentiment({ 'document': document });

}

function makeItem(element) {
    item = {
        title: element.title,
        link: element.link,
        date: element.pubDate,
        description: element.description,
        content: element["content:encoded"] || null, // prevent undefined insert into Firebase DB
        creator: element["dc:creator"],
        media: []
    }

    element.enclosure.forEach(enclosure => {
        item.media.push({
            url: enclosure.$.url,
            type: enclosure.$.type
        })
    });

    return item;
}

function cleanUp(data) {  
    // Empty array to add clean up elements
    const items = []
    // We are only interested in 'channel' children
    const channel = data.rss.channel

    channel.item.forEach(element => {
        item = {
            title: element.title,
            description: element.description,
            date: element.pubDate,
            creator: element["dc:creator"],
            media: []
        }

        // Iterates through all the elements named '<media:content>' extracting the info we care about
        element["media:content"].forEach(mediaContent => {
            item.media.push({
                url: mediaContent.$.url,                // Parses media:content url attribute
                credit : mediaContent["media:credit"]._ // Parses media:cretit tag content
            })
        });

        items.push(item);
    });

    return items;
}
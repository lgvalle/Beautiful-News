var functions = require('firebase-functions');
var Client = require('node-rest-client').Client;
var client = new Client();
const ONE_HOUR = 3600000
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.fetch = functions.https.onRequest((req, res) => {
    console.log("function starting");
    var lastEdition = admin.database().ref('/feed/last');
    return lastEdition.once("value")
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
    client.get("https://ep00.epimg.net/rss/elpais/portada.xml", function (data, response) {
        console.log("feed fetched");
        const items = parseChannel(data.rss.channel)
        const now = new Date(Date.now()).toISOString()

        return lastEdition
            .set({
                date: now,
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

    channel.item.forEach(element => {
        const item = makeItem(element);
        element.enclosure.forEach(enclosure => {
            item.media.push({
                url: enclosure.$.url,
                type: enclosure.$.type
            })
        });

        items.push(item);
        console.log("item parsed")
    });
    return items;
}

function makeItem(element) {
    return {
        title: element.title,
        link: element.link,
        date: element.pubDate,
        description: element.description,
        content: element["content:encoded"] || null, // prevent undefined insert into Firebase DB
        creator: element["dc:creator"],
        media: []
    }
}
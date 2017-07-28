var functions = require('firebase-functions');
var Client = require('node-rest-client').Client;
var client = new Client();

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.fetch = functions.https.onRequest((req, res) => {
    console.log("function starting");
    var lastEdition = admin.database().ref('/feed/last');
    lastEdition.once("value")
        .then(snapshot => parse(snapshot, res, lastEdition));
});

function parse(snapshot, res, lastEdition) {
    var exists = (snapshot.val() !== null);

    if (exists) {
        console.log("Exist -> return from DB")
        response(res, 200, snapshot.val());

    } else {
        console.log("Missing -> fetch")
        client.get("https://ep00.epimg.net/rss/elpais/portada.xml", function (data, response) {
            console.log("feed fetched");
            const items = parseChannel(data.rss.channel)
            const editionFullDate = new Date(data.rss.channel.pubDate).toISOString()
            const editionDate = editionFullDate.split('T')[0]
            console.log("edition date: " + editionDate)

            lastEdition.child(editionDate)
                .set({ date: editionFullDate, items: items })
                .then(() => console.log("items pushed to firebase"))
                .then(() => response(res, 201, items));
        });
    }
}

function response(res, code, elements) {
    return res.status(code)
        .type('application/json')
        .send(elements);
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
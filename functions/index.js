var functions = require('firebase-functions');
const URL_THE_GUARDIAN = "https://www.theguardian.com/uk/london/rss"

var Client = require('node-rest-client').Client;
var client = new Client();

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.fetchGuardian = functions.https.onRequest((req, res) => {
    client.get(URL_THE_GUARDIAN, function (data, response) {
        const items = cleanUp(data)
        res.status(201)
            .type('application/json')
            .send(items)
    });
});

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
                credit: mediaContent["media:credit"]._ // Parses media:cretit tag content
            })
        });
        items.push(item);
    });
    return items;
}
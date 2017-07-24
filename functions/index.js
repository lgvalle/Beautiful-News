var functions = require('firebase-functions');
var Client = require('node-rest-client').Client;
var client = new Client();

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.fetch = functions.https.onRequest((req, res) => {
    console.log("function starting");

    var lastEdition = admin.database().ref('/manufacturer/sony/messages/last');
    lastEdition.once("value")
        .then(function (snapshot) {
            var exists = (snapshot.val() !== null);

            if (exists) {
                console.log("Exist -> return from DB")
                response(res, snapshot.val())
                
            } else {
                console.log("Missing -> fetch")
                // Grab the current value of what was written to the Realtime Database.
                client.get("https://ep00.epimg.net/rss/elpais/portada.xml", function (data, response) {
                    // parsed response body as js object 
                    console.log("feed fetched");
                    // raw response 
                    //console.log(response);


                    const channel = data.rss.channel
                    const items = []

                    channel.item.forEach(element => {
                        const item = {
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

                        items.push(item);
                        console.log("item parsed")
                    });

                    lastEdition.push().set(items).then(snapshot => {
                        // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
                        console.log("items pushed to firebase");
                    });

                    response(res, items);
                });
            }
        });
});

function response(res, items) {
    res.status(200)
        .type('application/json')
        .send(items);
}

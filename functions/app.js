var Client = require('node-rest-client').Client;
var client = new Client();

var request = require('request');

request('https://ep00.epimg.net/rss/elpais/portada.xml', function (error, response, body) {
    if (!error && response.statusCode == 200) {
        console.log("it works!");
        console.log(body);
    }
})

/*
// direct way 
client.get("https://ep00.epimg.net/rss/elpais/portada.xml", function (data, response) {
    // parsed response body as js object 
    console.log(data.rss.channel.item[0].title);
    // raw response 
    //console.log(response);

    const channel = data.rss.channel

    channel.item.forEach(element => {
        const item = {
            title: element.title,
            link: element.link,
            date: element.pubDate,
            description: element.description,
            content: element["content:encoded"],
            creator: element["dc:creator"],
            media: []
        }

        element.enclosure.forEach(enclosure => {
            item.media.push({
                url: enclosure.$.url,
                type: enclosure.$.type
            })
        });
        console.log(item)

        
    });
});
*/
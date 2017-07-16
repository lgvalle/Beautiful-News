var Client = require('node-rest-client').Client;

var client = new Client();

// direct way 
client.get("http://ep00.epimg.net/rss/elpais/portada.xml", function (data, response) {
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

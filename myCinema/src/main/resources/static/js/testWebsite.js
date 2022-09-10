const title = document.getElementById('title');
const movieContainer = document.getElementById('movieContainer');
const output = document.getElementById('output');


// TODO: handle errors properly

/**
 * Fetches data from api and handles response. 
 * 
 * @param endpoint the url to send the request to.
 */
async function getRequest(endpoint) {
    try {
        const response = await fetch(endpoint, {
            headers: {
                'Content-Type' : 'application/json'
            }
        });

        const jsonResponse = await response.json();

        if (response.ok) {
            placeDataInHtml(jsonResponse);          

        } else {
            alert(response.status);
        }

    } catch(error) {
        alert(error);
    }
}


/** 
 * Feeds html page with data.
 * 
 * @param data an array of json objects, each object containing one movie.
*/
function placeDataInHtml(data) {
    data.forEach(movie => {
        const div = document.createElement('div');
        div.innerText = movie.title + ', ';
        div.innerText += 'with ' + movie.cast[0] + ' and ' + movie.cast[1];
        movieContainer.appendChild(div);
        lineBreak(div);
    });
}


/** 
 * Adds a line break. 
 * Needs to add two 'br' tags in order to get one actual line break for some reason.
 * 
 * @param parent the node to append the break to. 
*/
function lineBreak(parent) {

    const brake = document.createElement('br');
    parent.appendChild(brake);

    const brake2 = document.createElement('br');
    parent.appendChild(brake2);
}
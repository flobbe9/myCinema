import fetch from "node-fetch";


/**
 * Defines the object that is passed into the fetch method as second argument {@link fetch()}.
 */
interface FetchHeaders {
    method: string,
    headers: {
        "Content-Type" : string
    },
    body?: any
}


/**
 * Can make a request with any method to any url via fetch api. Returns the response in json or the fetch error.
 * 
 * @param url to send the request to.
 * @param fetchData the information the fetchAPI needs {@link FetchHeaders}.
 * @returns the recieved response, nothing in case of void or and error.
 */
async function makeRequest(url: string, fetchHeaders: FetchHeaders) {
    try {
        // stringifying requestBody
        if (fetchHeaders.body) fetchHeaders.body = JSON.stringify(fetchHeaders.body);

        // sending request and awaiting response
        const response = await fetch(url, fetchHeaders);
        
        // getting jsonResponse if statusCode 200
        if (response.ok) {
            return await response.json();
        } else {
            // TODO: implement proper error handling method
            console.log(`An error ocurred! Status code: ${response.status}`);
        }

    } catch (e) {
        console.log(e);
        return e;
    }
}


export { makeRequest };
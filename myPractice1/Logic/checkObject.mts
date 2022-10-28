/**
 * Checks any object or iterable for falsy values.
 *      
 * @param obj to check.
 * @returns false if:
 * - 'obj' is null,                                                                      
 * - 'obj' is undefined,                                                                      
 * - 'obj' is NaN,                                                                      
 * - 'obj' is a string with length 0                                                                      
 * - 'obj' is an iterable and any of the above applies to one element in the iterable.
 * - 'obj' is an object and any of the above applies to one element in the object.
 */
function objectValid(obj: any): boolean {
    // checking for null and undefined (objects may have to be null...)
    if (obj === null || obj === undefined) 
        return false;
    
    // checking for NaN
    if (typeof obj === "number" && isNaN(obj)) 
        return false;

    // checking for emtpy string
    if (typeof obj === "string" && obj === "") 
        return false;
    
    // iterating collection recursively, if iterable
    if (typeof obj[Symbol.iterator] === "function" && typeof obj !== "string")
        return !obj.some(el => !objectValid(el));

    // iterating object recursively
    if (typeof obj === "object") {
        return !Object.entries(obj).some((key, value) => {
            return !objectValid(key) || !objectValid(value);
        });
    }

    return true;
}

/**
 * Removes all space characters in front of a string and returns the modified version.
 * Leaves any other space character in the string as it is.         
 *  
 * @param str to remove spaces from.
 * @returns the modified string.
 */
function removeSpacesInFront(str: string): string {
    let idx = -1;
    [...str].some((char, index) => {
        idx = index;
        return char !== " ";
    });

    return str.substring(idx);
}


export { objectValid, removeSpacesInFront };
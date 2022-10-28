import express, { application } from "express";
import { makeRequest } from "../Logic/fetchAPI.mjs";
import { springUrl } from "../main.mjs";
import { objectValid, removeSpacesInFront } from "../Logic/checkObject.mjs";


const router = express.Router();


// defining Professor object
interface Professor {
    userName: string,
    password: string,
    firstName: string,
    lastName: string,
    birthday: string,
    role?: AppUserRole,
    salary: number
}

enum AppUserRole {
    USER,
    ADMIN
}


/**
 * Get professor by userName.
 */
router.get("/getByUserName", async (req, res, next) => {
    // calling spring api
    const url = springUrl + req.originalUrl;

    const fetchHeaders = {
        method: "get",
        headers: {
            "Content-Type": "applicatoin/json"
        }
    }

    const response = await makeRequest(url, fetchHeaders);

    // sending response
    res.send(response);
});


/**
 * Post new professor.
 */
router.post("/addNew", async(req, res, next) => {
    try {
        // checking if request body exists
        if (!req.body) 
            throw "Missing request body.";

        // validating request body 
        const professor: Professor = req.body;
        if (!objectValid(professor)) 
            throw "Something wrong with request body.";
        
        const url = springUrl + req.originalUrl;
        const fetchHeaders = {
            method: "post",
            headers: {
                "Content-Type": "application/json"
            },
            body: professor
        }

        // calling spring api
        const response = await makeRequest(url, fetchHeaders);

        res.send(response);

    } catch (e) {
        res.send(e);
    }
});


export default router;
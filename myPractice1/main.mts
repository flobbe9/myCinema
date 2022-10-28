import express from "express";
import rootRouter from "./Controller/root.mjs";
import professorRouter from "./Controller/professor.mjs"

export const springUrl = "http://localhost:4002";


const app = express();

// parsing json where needed
app.use(express.json());


// router: "/"
app.use("/", rootRouter);


// router: "test/appUser/professor"
app.use("/test/appUser/professor", professorRouter);


// handling wrong urls
app.use("*", (req, res, next) => {
    // TODO: error handling page
    res.send(`Something wrong with this url. Status ${req.statusCode}.`);
});


app.listen(4001, () => console.log("Server is listening on port 4001."));
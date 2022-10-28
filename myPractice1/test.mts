import { makeRequest } from "./Logic/fetchAPI.mjs";

// testing fetch
const fetchHeaders = {
    method: "post",
    headers: {
        "Content-Type" : "application/json"
    },
    body: {
        userName: 'florin.schikarski@gmail.com',
        password: '$2a$31$17b3C.a25sV6TmiAnkZwUuW8I1kUlehrKjfnBNtS9jqetJbXYTdAm',
        firstName: 'Florin',
        lastName: 'Schikarski',
        birthday: "2001-02-12",
        salary: 520,
    }
}
makeRequest("http://localhost:4002/test/appUser/professor/addNew", fetchHeaders);
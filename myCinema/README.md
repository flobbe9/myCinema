# myCinema
Spring-boot project with a made up cinema website that contains a few movies and (theoretically) offers tickets. 

Uses postgreSQL for the db.

## Run myCinema in docker
To start the api, just get the 'docker' folder, it contains a .jar file with the spring-boot project. 

Once you bashed into the 'docker' folder, use ´´´docker-compose up´´´ and docker-compose will do the rest. 

## Login
There are 2 users pre registered for login:

Role: **USER**
username: schikarski98@gmail.com 
password: user

Role: **ADMIN**
username: florin.schikarski@gmail.com 
password: admin

### A few endpoints to try:
Start page: https://localhost:4001

Get all movies: https://localhost:4001/movie/getAll
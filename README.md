# GSG demo

Demo Spring Boot & Angular 7 project

## Tech stack

### Backend
1) Java 11
2) Spring Boot
3) Maven

### Frontend
1) Typescript
2) Angular 7
3) Angular CLI
4) NPM

## Build & Run

Considering you have all requirements installed.

### Backend

In order application to work you will need to provide `youtube api key`.

Open your terminal:

Navigate to `testproject` and run the following:        
  1)   `mvn clean install`  (this will compile, build and test backend)
  2)   `mvn spring-boot:run -Dspring-boot.run.arguments=--youtube.api.key=API_KEY` 

Note: remember to change `API_KEY` to your key.

#### Running tests

To run tests use this command:      
`mvn clean test`

### Frontend

Navigate to `testproject-ui` and run the following:        
  1)   `npm install`  (this will install all dependencies)
  2)   `ng serve` 
  
#### Running tests

To run tests use this command:      
`npm run test-headless`
  
## Play with it

Open you browser and go to http://localhost:4200

Test user credentials:      
Username: `test@gmail.com`      
Password: `test`

Enjoy :)

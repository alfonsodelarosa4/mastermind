# Mastermind Project Submission
## Overview
I'm Alfonso De La Rosa. This is my project submission for the LinkedIn Apprenticeship: Mastermind. Mastermind is a game, where the player guesses the number combinations. This is a fullstack project that uses React for the frontend and Java Spring for the backend. The Java Spring app accepts REST and GraphQL api requests from the client and supports WebSocket communication with the client. In addition, the Java Spring app stores game sessions to Redis and game events to MongoDB.

## Building and Running the application
I developed, build, and ran my application on a Windows computer.
### 1. Required technologies NodeJS, Gradle, JDK 21, and Docker
The following technologies are required to run the application:
1. **NodeJS**: Installers are provided below (for Linux, macOS, Windows):
    - [NodeJS Downloads](https://nodejs.org/en/download/current)
2. **JDK 21**: Installers are provided below (for Linux, macOS, Windows):
    - [Java 21 Downloads](https://www.oracle.com/java/technologies/downloads/#java21)
3. **Gradle**: Instructions are provided below (for Linux, macOS, Windows):
    - [Gradle Installation Guide](https://gradle.org/install/)
    - includes manual installation and package manager
4. **Docker**: Instructions are provided below (for Linux, macOS, Windows):
    - [Docker Installation](https://docs.docker.com/get-docker/)

### 2. Running Docker containers of Redis and MongoDB
1. Open a terminal
2. Run Docker container of Redis
```
docker run -d --name redis-container -p 6379:6379
```
3. Run Docker container of MongoDB
```
docker run -d --name mongo-container -p 27017:27017
```
### 3. Build and run Java Spring App with Gradle
1. Open a terminal (or use the same one)
2. Start from the root directory of the repository
3. Build the Java Spring app with Gradle
```
cd mastermind-backend
gradle clean build
```
4. Run Java Spring app with Gradle
```
gradle run
```
5. Now leave this terminal alone
### 4. Run React app
1. Open another terminal. (It cannot be the same one as the previous)
2. Start from the root directory of the repository
3. Run React app with npm
```
cd mastermind-client
npm start
```
4. Now leave this terminal alone
### 5. How to use the application
1. [Click here to open the React app](http://localhost:3000/)
2. This is the game start screen. You simply click on the blue button to start playing.
3. You have 10 attempts to guess the correct number. Enter your guess in the text box. Then press enter to submit.
4. After pressing submit, you should receive a response from the Spring server

## Development Process
### 1. Understanding the challenge
Before doing anything, I spent time reading the challenge requirements and made sure that I understood what was being asked. These were my findings:

**USER:**
- The user will play "against" the computer and try to guess the number of combinations
- After each attempt, the computer will provide feedback whether the player had guess a number correcly.
- Player must guess the right number of combinations within 10 attempts.

**SERVER:**
- At game start, server will generate 4 nums from a total of 8 different numbers
- Server must use Random generator API
- Server will generate a feedback response.
    - The player had guess a correct number
    - The player had guessed a correct number and its correct location
    - The player’s guess was incorrect

**USER INTERFACE:**
- Player must have a way of interacting with game to perform the following:
    - to guess the combinations of 4 numbers
    - to view history of guesses and feedback
    - to view number of guesses remaining is displayed.

**EXTENSIONS:**
- We are encouranged to expand the game to showcase talents and passion.

**FEEDBACK:**
- I realized the meaning of each value: correct number and correct location. After reading the examples, I made the following findings:
    - correct number: # of distinct numbers in guess that are in the answer
    - correct location: # of digits in guess that match with corresponding answer
Conclusion: I understand the requirements of the challenge to implement the project.
### 2. Technologies to use
After understanding the requirements, I took into account of the technologies I was already familiar with and labeled its application to the project:
- Java Spring as the application framework for the backend
- REST for client-to-server communication
- GraphQL for client-to-server communiation
- MongoDB for storing game events
- Docker for containers
- React and Redux for the frontend and user interface

Even though these technologies are great for implementing backend applications, I still need to use technologies that would support real-time communications for game sessions that last at most an hour. Therefore, I started thinking about the following technologies:
- Redis for saving game sessions
- WebSocket for bidirectional communication between the client and server

Even though I have not had experiences working with these technologies, I spent several hours understanding how Java Spring can utilize these technologies.
### 3. Tradeoffs and decisions
**COMMUNICATION:**
- **REST**: REST is ideal when a client want to access simple data sources. Therefore, REST will be used to start and retrieve the game session information.
- **GraphQL:** GraphQL is ideal when accessing large data sources, utilizing customize queries, and preventing over-fetching. Therefore, GraphQL will be used in retrieving the game events of a game session, such as the guesses of a user and the feedback of the server. This also prevents over-fetching because not all of the information will be necessary, such as the ids of game event and game session.
- **WebSocket**: WebSocket is ideal when for real-time communication between the clients and server. In addition, websocket has low latency and reduced overhead compared to REST. Therefore, the client and server will utilize websocket communiation to submit guesses and provide feedback, respectively.

**STORAGE:**
- SQL: SQL is ideal for data integrity since it follows ACID principles.
- NoSQL: NoSQL is ideal for availibility and requiring different forms of data stored. Since the Mastermind Project is a real-time game, NoSQL will be utilized for this project due to its availability.

### 4. Backend Design
The Java Spring application can be separated to the following components: configurations, controllers, model, repository, and services.

**CONFIGURATIONS:**
Since configurations are used to configure settings for components of the backend, the following configurations were used:
To provide context: Beans are active components in the backend that are managed by Java Spring.
- RedisConfiguration: used to create templates as the means to communicate to the Redis container for CRUD operations.
- WebConfiguration: used to permit GraphQL requests from localhost:3000, which is where the React app is located.
- WebSocketConfiguration: used for creating different endpoints for the client to establish a connection, to subscribe to a topic, and to send messages via WebSocket.

**CONTROLLERS:**
Since controllers are used to handle incoming requests and respond to them, the following controllers were created:
- ApiRestController: used to handle REST requests from the client, such as starting a game session.
- GraphQLController: used to handle GraphQL requests from the client, such as retrieving a list of game events of a session.
- WebSocketController: used to handle incoming WebSocket messages from the client, such as making a guess.

**MODELS:**
The following Java classes were created to be used as models:
- GameEvent: stores important game events during a session, such as a description of what happened, along with the timestamp and game session id. GameEvent entries are stored in MongoDB.
- GameSession: contains information of each game session, such as the game session id, the answer itself, the number of attempts left, etc.

**REPOSITORIES:**
The following respositories were used to interact with MongoDB and Redis.
- GameEventRepository: provides CRUD functions to store game events to MongoDB.
- GameSesssionRepsitory: custom repository that provides CRUD functions to store game sesesion to Redis.

**SERVICES:**
THe following are services that contain business logic:
- GameEventService: provides simplified functions by interacting with GameEventRepository with error-handling.
- GameSessionService: provides simplified functions by interacting with GameSessionRepository with error-handling.
- RandomApiService: provides functions that communicate to the Random Integer API of random.org
- WebSocketMessagingService: provides functions that send messages to websocket topics, such as game sessions.

### 5. Frontend Design
The React app will provide the user-interface of interacting with the backend. It utilizes the following technologies to provide an simplified experience of Mastermind:
- redux: state management of game sessions.
- bootstrap: provides components made by Bootstrap.
- axios: library that makes REST api calls to the backend server.
- apollo-client: library that executes GraphQL queries and mutations to the backend server.
- react-router: enables client-side routing to navigate different webpages.
- sockjs, socketclient, react-stomp: enables websocket communication between client and server.

### 6. Backend Workflow
**GAME START:** 
1. Client presses the "Play Mastermind" button
2. Client makes a REST POST call at /game-session endpoint to create a game session
3. Backend creates a game session via the GameSessionService
    - which then makes an API call to the Random Integer endpoint
4. Backend creates a game event via the GameEventService to record creation of game start
5. Backend returns the game session id and number of attempts to the user
6. Client then takes the user to the game webpage to play mastermind

**GUESS:**
1. Client enters guess in text bok and presses "Submit" button.
2. Client sends a message to the /guess websocket mapping.
3. Backend receives game session id and guess from websocket message.
4. Backend retrieves game session via GameSessionService with id.
5. Backend creates game event of user's guess via GameEventService.

**CASE: user guesses answer correctly**
1. Backend creates game event of game won via GameEventService.
2. Backend sends websocket message to game session topic that game was won via WebSocketMessagingService.
3. The client subscribed to the game session topic will receive the message and display the outcome to the user.

**CASE: user guesses incorrectly but runs out of attempts**
1. Backend creates game event of game lost via GameEventService.
2. Backend sends websocket message to game session topic that game was lost via WebSocketMessagingService.
3. The client subscribed to the game session topic will receive the message and display the outcome to the user.

**CASE: user guesses incorrectly still has attempts**
1. Backend calculates the correct numbers and correct locations of guess.
2. Backend creates game event of feedback of guess via GameEventService.
3. Backend sends websocket message to game session topic of the feedback via WebSocketMessagingService.
4. The client subscribed to the game session topic will receive the message and display the outcome to the user.

### 6. Obstacles
**WEBSOCKET TOPICS:**
When utilizing websocket, a client must subscribe to a websocket topic to receive messages from the server. As a result, I had two ideas for what the topic should be: the username or game session. If the topics were usernames, the user will be able to receive personalized messages by subscribing to their username id, which is more ideal for messaging applicaitons. However, this will require more overhead in terms of handling username information when sending messages. On the other hand, if the topics were game sessions, the client would be able to receive messages relating to the game events in the game session. Therefore, the game session were used as websocket topics.

**GAME SESSION ID:**
When implementing the game session ids, I had to ideas of what the id could be, such as a long/UUID value or a small string value of randomized characters. I chose to implement the game session ids as small string values since the game sessions are transient and might be difficult for the user to remember.

**REDIS REPOSITORY:**
Redis is usually used to cache information retrieved from a separate database, such as SQL and NoSQL. However, I needed to store the game session data only to Redis since game session are transient and will be necessary for an hour. To provide context: Repositories provide simple functions to interact with the database. Even though there weren't premade repositories, I implemented my own repository to interact with Redis by creating simple functions to access Redis.

**REACT WEBSOCKET:**
Even though the main focus of the project is the backend, the frontend did not have premade React components  to utilize immediately for Stomp websocket for Java Spring. Therefore, I created a provider and context to subscribe to the game session topic and send messages to the server at different websocket mappings, so that the child components can be notified of new websocket messages to display them and to send websocket messages at different endpoints.
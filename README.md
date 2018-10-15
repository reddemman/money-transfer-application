A Java RESTful API for money transfers between users accounts

Technologies:

1)JAX-RS API
2)Core Java
3)Jetty Container
4)Maven
5)Junit
6)Eclipse IDE


How to Run:

1)ApplicationMain.java is a standalone class to run the application which you can run a java application.Application starts a jetty server on localhost with port 9999.

Note: Use any Rest Client tools to check the results(i used "PostMan")

URL:

http://localhost:9999/user/test1

http://localhost:9999/user/test2

http://localhost:9999/account/1

http://localhost:9999/account/2


HTTP METHOD	PATH	to available services:

GET	/user/{userName}	get user by user name

GET	/user/all	get all users

POST	/user/create	create a new user

PUT	/user/{userId}	update user

DELETE	/user/{userId}	remove user

GET	/account/{accountId}	get account by accountId

GET	/account/all	get all accounts

GET	/account/{accountId}/balance	get account balance by accountId

POST	/account/create	create a new account

DELETE	/account/{accountId}	remove account by accountId

PUT	/account/{accountId}/withdraw/{amount}	withdraw money from account

PUT	/account/{accountId}/deposit/{amount}	deposit money to account

POST	/transaction	perform transaction between 2 user accounts


JSON:

User :

{  
  "userId": 1
  
  "userName":"user1",
  
  "emailAddress":"user1@gmail.com"
  
} 

User Account:

{  
   "accountId": 1
   
   "userName":"user1",
   
   "balance":10.0000,
   
   "currencyCode":"GBP"
} 

User Transaction:

{  
   "currencyCode":"EUR",
   
   "amount":100000.0000,
   
   "fromAccountId":1,
   
   "toAccountId":2
}

Http Status Codes:

200 OK: The request has succeeded

400 Bad Request: The request could not be understood by the server

404 Not Found: The requested resource cannot be found

500 Internal Server Error: The server encountered an unexpected condition

Tests:

1) There are 3 test scripts in the application for Accounts, User and Transactions (TestAccountService.java,TestUserService.java and TestTransactionService.java). These tests are run as a Junit tests





 

 



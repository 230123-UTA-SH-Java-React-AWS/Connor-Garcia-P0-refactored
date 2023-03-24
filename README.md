##What is this project?

This is a complete migration of my original P0 project. It has the same functionality as that project, but instead of erverything being done "manually" (setting up JDBC connections, defining HTTP connections etc.) everything in this project was handled using Spring Boot. This took a few hours in one day to make and it was so much easier than doing everything manually. Thanks, Spring.
 
###How to use

You'll need to set these three environment variables. You may change the actual values; the ones provided below are examples.
 
 - spring.url: jdbc:postgresql://localhost:5432/project0refactor
 - spring.username: postgres
 - spring.password: password123

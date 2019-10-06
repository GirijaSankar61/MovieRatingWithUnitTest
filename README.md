![CircleCI](https://img.shields.io/circleci/build/github/GirijaSankar61/MovieRatingWithUnitTest/master?token=799d33a3cef73bdab72f5c1e1cd577e9ab8703cf)

Integration Testing as well as unit testing with spring boot application

Unit testing
=============
Here we test each method of one controller and service without any dependency on other component.
whatever dependency are there we tried to mock it first,then inject to the testable code and then start test.

Integration testing
=============

Here we allow spring to autowire or inject dependency like in real time.Here we are not directly mock the dependencies because actual object will be injected by spring.With the help of mockmvc we directly create get,post,put request and fetch response from our resources.


Docker with MYSQL
==================

## Useful Docker commands

-`docker build -t myapp .`
</br>
</br>
-`docker run -p 5084:5055 myapp -e spring.datasource.username = <username> -e spring.datasource.password = <password> -e spring.datasource.url=jdbc:mysql:<your url>:<port no>/test?allowPublicKeyRetrieval=true&useSSL=false`


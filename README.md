![CircleCI](https://img.shields.io/circleci/build/github/GirijaSankar61/MovieRatingWithUnitTest/master?token=5acbf8684c82fd2d51e613d7106125bb5a023bb7)

Integration Testing as well as unit testing with spring boot application

Unit testing
=============
Here we test each method of one controller and service without any dependency on other component.
whatever dependency are there we tried to mock it first,then inject to the testable code and then start test.

Integration testing
=============

Here we allow spring to autowire or inject dependency like in real time.Here we are not directly mock the dependencies because actual object will be injected by spring.With the help of mockmvc we directly create get,post,put request and fetch response from our resources.


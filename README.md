## community-backend-v2
* Deal with web community backend applications


## Project structure

### core
#### core-utils
* The module for common utilities

#### core-web
* The module for common web configurations

### domain
#### domain-rds
* The module for community applications' models and entities about RDBMS

### application
#### community-gateway-app
* The gateway server for other microservices to pass through

#### community-discovery-app
* The server for registering and discovering microservices

#### community-auth-app
* The server for user authentication and authorization 

#### community-service-app
* The server for community operations


## Skills
* Java 11
* Spring Boot 2.5.x
* Spring Security
  * Spring Security OAuth2
  * Spring Security JWT
* Spring Boot Actuator
* Spring Cloud
  * Spring Cloud Netflix Eureka
  * Spring Cloud Gateway
  * HTTP Request 
    * Feign Client
  * Circuit Breaker
    * Resilience4j
* Spring Rest Docs
* Docker
  * docker-compose
* PlantUML

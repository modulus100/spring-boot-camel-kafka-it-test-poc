# spring-boot-camel-kafka-it-test-poc
Super minimalistic example of how apache camel kafka module can 
be tested with Spring Boot and embedded kafka  
Tested with Java 11 and Java 16

### Test
```
./gradlew test
```

### Camel route
Gets executed 2 times with a period 500ms

![alt text](img/route.png)


### Kafka consumer test
![alt text](img/test2.png)

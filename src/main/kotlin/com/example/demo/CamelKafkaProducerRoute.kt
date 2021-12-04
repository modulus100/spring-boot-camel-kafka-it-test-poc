package com.example.demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

data class User(val name: String = Faker().name().name())

@Component
class MyRoute : RouteBuilder() {

    override fun configure() {
        from("timer:foo?period=500&repeatCount=2")
            .process {
                it.`in`.body = ObjectMapper().writeValueAsString(User())
            }
            .to("kafka:topic.test?brokers=localhost:9092")
    }
}
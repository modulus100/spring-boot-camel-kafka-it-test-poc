package com.example.demo

import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import java.time.Duration
import java.util.concurrent.TimeUnit


@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"],
    controlledShutdown = false,
    topics = ["topic.test"]
)
class CamelKafkaProducerRouteTest {

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Test
    fun `consume 2 users in 1 second`() {
        // Arrange
        val consumerProps = KafkaTestUtils.consumerProps(
            "group_consumer_test",
            "false",
            embeddedKafkaBroker
        )

        consumerProps[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

        val consumerFactory = DefaultKafkaConsumerFactory(
            consumerProps,
            StringDeserializer(),
            JsonDeserializer(User::class.java)
        )

        Thread.sleep(1000)

        val consumer: Consumer<String, User> = consumerFactory.createConsumer()
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "topic.test")

        // Act
        val consumerRecords: ConsumerRecords<String, User> = consumer.poll(Duration.ofSeconds(1))

        // Assert
        Assertions.assertEquals(2, consumerRecords.count())
    }
}
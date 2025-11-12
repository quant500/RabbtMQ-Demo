package com.meichel.kafkademo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
@DirtiesContext
@Import({TestcontainersConfiguration.class, KafkaConfig.class})
class KafkaContainerTest {

    static {
        // Set DOCKER_HOST for Testcontainers before any container initialization
        System.setProperty("DOCKER_HOST", "tcp://0.0.0.0:2376");
        System.setProperty("testcontainers.ryuk.disabled", "true");
    }

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MessageConsumer messageConsumer;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", TestcontainersConfiguration.kafka::getBootstrapServers);
    }

    @Test
    void shouldSendAndReceiveMessage() {
        String testMessage = "Hello Kafka!";

        // Reset the counter before sending message
        messageConsumer.resetCounter();

        messageProducer.sendMessage(testMessage);

        // Wait for the message to be received by the consumer
        await().atMost(60, TimeUnit.SECONDS)
               .until(() -> messageConsumer.getCounter().getCount() == 0);

        // Verify that the message was received
        assertThat(messageConsumer.getCounter().getCount()).isEqualTo(0);
        System.out.println("✅ Kafka message successfully sent and received!");
    }

    @Test
    void shouldStartKafkaContainer() {
        try {
            // Test that the container is running and accessible
            assertThat(TestcontainersConfiguration.kafka.isRunning()).isTrue();

            // Test that bootstrap servers are available
            String bootstrapServers = TestcontainersConfiguration.kafka.getBootstrapServers();
            assertThat(bootstrapServers).isNotNull();
            assertThat(bootstrapServers).contains("localhost");

            System.out.println("✅ Kafka Container is running at: " + bootstrapServers);
        } catch (Exception e) {
            System.err.println("❌ Container start failed: " + e.getMessage());
            System.err.println("Container state: " + TestcontainersConfiguration.kafka.getContainerInfo());
            throw e;
        }
    }
}

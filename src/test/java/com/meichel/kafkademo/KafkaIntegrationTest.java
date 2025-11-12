package com.meichel.kafkademo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@DirtiesContext
@Import({TestcontainersConfiguration.class, KafkaConfig.class})
class KafkaIntegrationTest {

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        // Set Kafka bootstrap servers from the container
        registry.add("spring.kafka.bootstrap-servers", TestcontainersConfiguration.kafka::getBootstrapServers);
    }

    @Test
    void shouldStartKafkaContainer() {
        // Test that the container is running and accessible
        assertThat(TestcontainersConfiguration.kafka.isRunning()).isTrue();

        // Test that bootstrap servers are available
        String bootstrapServers = TestcontainersConfiguration.kafka.getBootstrapServers();
        assertThat(bootstrapServers).isNotNull();
        assertThat(bootstrapServers).contains("localhost");

        System.out.println("✅ Kafka Container is running at: " + bootstrapServers);
    }

    @Test
    void shouldLoadApplicationContext() {
        // Test that Spring context loads successfully
        assertThat(true).isTrue(); // Basic assertion to ensure test runs
    }
}

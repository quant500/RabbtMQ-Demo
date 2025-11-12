package com.meichel.kafkademo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    public static final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:8.1.0"))
            .withExposedPorts(9092, 9093);

    @Bean
    ConfluentKafkaContainer kafkaContainer() {
        return kafka;
    }
}

package com.meichel.kafkademo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "kafka.topic.name=test-topic"
})
class KafkaDemoApplicationTests {

	@Test
	void contextLoads() {
	}

}

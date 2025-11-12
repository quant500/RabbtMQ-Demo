package com.meichel.kafkademo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class MessageConsumer {

    private CountDownLatch counter = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "demo-group")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
        counter.countDown();
    }

    public CountDownLatch getCounter() {
        return counter;
    }

    public void resetCounter() {
        this.counter = new CountDownLatch(1);
    }
}

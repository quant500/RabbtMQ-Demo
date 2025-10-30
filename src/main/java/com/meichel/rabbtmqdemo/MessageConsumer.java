package com.meichel.rabbtmqdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class MessageConsumer {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    private final CountDownLatch counter = new CountDownLatch(1);

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
        counter.countDown();
    }

    public CountDownLatch getCounter() {
        return counter;
    }
}

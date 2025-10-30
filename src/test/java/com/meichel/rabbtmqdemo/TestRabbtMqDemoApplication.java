package com.meichel.rabbtmqdemo;

import org.springframework.boot.SpringApplication;

public class TestRabbtMqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.from(RabbtMqDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

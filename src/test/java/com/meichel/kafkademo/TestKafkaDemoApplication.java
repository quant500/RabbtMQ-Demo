package com.meichel.kafkademo;

import org.springframework.boot.SpringApplication;

public class TestKafkaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.from(KafkaDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

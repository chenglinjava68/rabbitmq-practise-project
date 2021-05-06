package com.hualala.rabbitmqackmodel;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqAckModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqAckModelApplication.class, args);
    }

}

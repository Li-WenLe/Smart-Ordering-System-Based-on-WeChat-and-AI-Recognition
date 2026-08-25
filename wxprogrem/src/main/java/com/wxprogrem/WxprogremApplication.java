package com.wxprogrem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@EnableScheduling
@EnableAsync
@EnableRabbit
@SpringBootApplication
public class WxprogremApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxprogremApplication.class, args);
	}

}

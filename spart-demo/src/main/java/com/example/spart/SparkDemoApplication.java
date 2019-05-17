package com.example.spart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SparkDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkDemoApplication.class, args);
        //new SpringApplicationBuilder(SparkDemoApplication.class).web(WebApplicationType.NONE).run(args);
    }

}

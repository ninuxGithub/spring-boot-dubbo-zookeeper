package com.example.spart;

import com.example.spart.service.SparkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@SpringBootApplication
public class SparkDemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(SparkDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SparkDemoApplication.class, args);
        //new SpringApplicationBuilder(SparkDemoApplication.class).web(WebApplicationType.NONE).run(args);
    }

    @Autowired
    private SparkService sparkService;

    @RequestMapping(value = "/test")
    public Map<String, Object> test() {
        logger.info("info log");
        logger.debug("info log");
        return sparkService.wardCount();
    }

}

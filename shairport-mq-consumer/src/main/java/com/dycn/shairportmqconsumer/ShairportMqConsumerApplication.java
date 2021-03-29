package com.dycn.shairportmqconsumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication()
public class ShairportMqConsumerApplication {



    @Value("${user.xx}")
    private String test;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ShairportMqConsumerApplication.class, args);
        String userName = applicationContext.getEnvironment().getProperty("user.name");
        String userAge = applicationContext.getEnvironment().getProperty("user.age");
        String userxx = applicationContext.getEnvironment().getProperty("user.xx");
        System.err.println("user name :" +userName+"; age: "+userAge);
        System.err.println("user xx :" +userxx);
    }


    @RestController
    public class EchoController {
        @GetMapping(value = "/echo")
        public String echo() {
            return "Hello Nacos Discovery " + test;
        }
    }

}

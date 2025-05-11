package tn.esprit.cloud_in_mypocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class CloudInMyPocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudInMyPocketApplication.class, args);
    }

}

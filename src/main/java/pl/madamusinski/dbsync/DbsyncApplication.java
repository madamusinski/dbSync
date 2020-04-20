package pl.madamusinski.dbsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DbsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbsyncApplication.class, args);
    }

}

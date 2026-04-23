package TOPSECRET;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.out.printf("Insert a random char");
        SpringApplication.run(Main.class, args);
    }
}
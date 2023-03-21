package eu.senla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"eu.senla"})
public class RentalApplication {

    public static void main(String[] args) {
        try {

            SpringApplication.run(RentalApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

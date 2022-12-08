package rserenity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rserenity.ui.ConsoleController;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class})
public class AracKiralamaPortaliApplication {
    public static Scanner scanner;

    public static ConsoleController consoleController;

    @Autowired
    public AracKiralamaPortaliApplication(ConsoleController consoleController) {
        AracKiralamaPortaliApplication.consoleController = consoleController;
    }

    public static void main(String[] args){
        SpringApplication.run(AracKiralamaPortaliApplication.class, args);

        scanner = new Scanner(System.in);

        boolean systemActive = true;
        char input;

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while(systemActive){
            System.out.println("""
                    Select an option :\s
                    1 : User\s
                    2 : Admin\s
                    3 : Quit""");
            input = scanner.next().charAt(0);
            switch (input) {
                case '1' -> consoleController.userNullMenu();
                case '2' -> consoleController.adminMenu();
                case '3' -> systemActive = false;
                default -> System.out.println("Invalid operation !");
            }
        }
    }
}

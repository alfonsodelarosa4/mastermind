/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mastermind.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

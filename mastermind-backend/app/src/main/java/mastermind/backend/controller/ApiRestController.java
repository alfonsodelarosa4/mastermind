package mastermind.backend.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiRestController {

    @PostMapping("/")
    public ResponseEntity<String> createGameSession() {
        String message = "Hello world";
        return ResponseEntity.ok(message);
    }
}

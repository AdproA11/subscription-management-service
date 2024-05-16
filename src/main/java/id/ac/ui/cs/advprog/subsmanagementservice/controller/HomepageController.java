package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomepageController {

    @GetMapping("/")
    public ResponseEntity<String> homepage() {
        return ResponseEntity.ok("homepage");
    }

}
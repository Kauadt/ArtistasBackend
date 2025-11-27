package facul.artists.controllers;

import facul.artists.models.User;
import facul.artists.services.UserService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            User newUser = userService.registerWithPhoto(nome, email, password, photo);
            return ResponseEntity.ok(newUser);

        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User LoggedUser = userService.loginUser(user);

        if (LoggedUser != null) {
            return ResponseEntity.ok(LoggedUser);
        } else {
            return ResponseEntity.status(401).body("Erro ao logar");
        }
    }
}
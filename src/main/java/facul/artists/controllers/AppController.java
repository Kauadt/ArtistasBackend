package facul.artists.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    /**
     * Esta rota SÓ será acessível se o usuário estiver logado.
     */
    @GetMapping("/")
    public String home() {
        return "home"; // Corresponde a /resources/templates/home.html
    }

    /**
     * Esta rota é pública (configurada no SecurityConfig).
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // Corresponde a /resources/templates/login.html
    }
}
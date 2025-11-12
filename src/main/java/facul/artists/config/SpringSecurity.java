package facul.artists.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        // Permite acesso público a URLs específicas
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Todas as outras URLs exigem autenticação

                        .requestMatchers("/user/**").permitAll()

                        .anyRequest().authenticated())
                .formLogin(form -> form
                        // Define a URL da página de login (o Spring gera uma padrão se não
                        // especificarmos)
                        .loginPage("/login")
                        // A URL para onde o usuário é redirecionado após o login com sucesso
                        .defaultSuccessUrl("/", true)
                        .permitAll())

                .csrf(csrf -> csrf
                        // Diz ao Spring para IGNORAR a proteção CSRF
                        // nessas rotas que começam com /api/ e /user/
                        .ignoringRequestMatchers("/user/**"))

                .logout(logout -> logout.permitAll());

        return http.build();
    }
}

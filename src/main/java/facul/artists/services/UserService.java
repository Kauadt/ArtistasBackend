package facul.artists.services;

import facul.artists.models.User;
import facul.artists.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

 public User createUser(User user) {
        String senhaCriptografada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCriptografada);
        
        return userRepository.save(user);
    }

    public Optional<User> getByID(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        User userAtualizado = getByID(id).orElseThrow(() -> new RuntimeException("User not found"));
        userAtualizado.setNome(user.getNome());
        userAtualizado.setEmail(user.getEmail());

        return userRepository.save(userAtualizado);
    }

    public void deleteUser(Long id) {
        User user = getByID(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}

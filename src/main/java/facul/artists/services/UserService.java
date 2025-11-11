package facul.artists.services;

import facul.artists.models.User;
import facul.artists.repositories.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User crateUser(User user) {
        return userRepository.save(user);
    }

    public User getdByID(Long id){
        return userRepository.getReferenceById(id);
    }

    public User updateUser(Long id,User user) {
        User userAtualizado = getdByID(id);

        userAtualizado.setNome(user.getNome());
        userAtualizado.setEmail(user.getEmail());
        userAtualizado.setPassword(user.getPassword());

        return userRepository.save(userAtualizado);
    }

    public void deleteUser(Long id){
        User user =  getdByID(id);
        userRepository.delete(user);
    }
}

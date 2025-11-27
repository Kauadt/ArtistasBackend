package facul.artists.services;

import facul.artists.models.Artist;
import facul.artists.models.Artwork;
import facul.artists.models.User;
import facul.artists.repositories.ArtistRepository;
import facul.artists.repositories.ArtworkRepository;
import facul.artists.repositories.ChannelRepository;
import facul.artists.repositories.RatingRepository;
import facul.artists.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    private final ArtistRepository artistRepository;
    private final ArtworkRepository artworkRepository;
    private final RatingRepository ratingRepository;
    private final ChannelRepository channelRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            FileStorageService fileStorageService,
            ArtistRepository artistRepository,
            ArtworkRepository artworkRepository,
            RatingRepository ratingRepository,
            ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
        this.artistRepository = artistRepository;
        this.artworkRepository = artworkRepository;
        this.ratingRepository = ratingRepository;
        this.channelRepository = channelRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Este email já está em uso.");
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public Optional<User> getByID(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        User updatedUser = getByID(id).orElseThrow(() -> new RuntimeException("User not found"));
        updatedUser.setNome(user.getNome());
        updatedUser.setEmail(user.getEmail());

        return userRepository.save(updatedUser);
    }

    public User loginUser(User userLogin) {
        Optional<User> user = userRepository.findByEmail(userLogin.getEmail());

        if (user.isPresent()) {
            User LoggedUser = user.get();
            boolean passwordValidation = passwordEncoder.matches(
                    userLogin.getPassword(),
                    LoggedUser.getPassword());

            if (passwordValidation) {
                return LoggedUser;
            }
        }
        return null;
    }

    public User registerWithPhoto(String nome, String email, String password, MultipartFile photo) throws IOException {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Este email já está em uso.");
        }

        User user = new User();
        user.setNome(nome);
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(password));

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileStorageService.saveImage(photo);
            user.setProfilePhotoUrl("/files/" + fileName);
        }

        return userRepository.save(user);
    }

    public User updateProfilePhoto(Long id, MultipartFile photo) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String fileName = fileStorageService.saveImage(photo);
        user.setProfilePhotoUrl("/files/" + fileName);

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        ratingRepository.deleteByUserId(id);
        channelRepository.deleteByUser1IdOrUser2Id(id, id);

        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getProfilePhotoUrl() != null)
            fileStorageService.deleteFile(user.getProfilePhotoUrl());

        Artist artist = artistRepository.findByUserId(id).orElse(null);
        if (artist != null) {
            List<Artwork> artworks = artworkRepository.findByArtist_ArtistId(artist.getArtistId());
            for (Artwork art : artworks) {
                if (art.getContentUrl() != null)
                    fileStorageService.deleteFile(art.getContentUrl());
                ratingRepository.deleteByArtwork_ArtWorkId(art.getArtWorkId());
                artworkRepository.delete(art);
            }
            artistRepository.delete(artist);
        }

        userRepository.deleteById(id);
    }

}

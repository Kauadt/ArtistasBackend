package facul.artists.controllers;

import facul.artists.models.Artist;
import facul.artists.models.User;
import facul.artists.repositories.ArtistRepository;
import facul.artists.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ArtistRepository artistRepository;

    public UserController(UserService userService, ArtistRepository artistRepository) {
        this.userService = userService;
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public List<User> listAll() {
        return userService.getAll();
    }

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User product) {
        return userService.updateUser(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable Long id) {
        return userService.getByID(id);
    }

    @PostMapping("/{id}/profile-photo")
    public User uploadProfilePhoto(
            @PathVariable Long id,
            @RequestParam("photo") MultipartFile photo) throws IOException {
        return userService.updateProfilePhoto(id, photo);
    }

    @GetMapping("/{id}/isArtist")
    public ResponseEntity<?> checkIfUserIsArtist(@PathVariable Long id) {
        Optional<Artist> artist = artistRepository.findByUserId(id);

        if (artist.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "isArtist", true,
                    "artist", artist.get()));
        } else {
            return ResponseEntity.ok(Map.of("isArtist", false));
        }
    }

}

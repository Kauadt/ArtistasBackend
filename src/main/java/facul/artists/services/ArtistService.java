package facul.artists.services;

import facul.artists.models.Artist;
import facul.artists.models.Artwork;
import facul.artists.models.User;
import facul.artists.repositories.ArtistRepository;
import facul.artists.repositories.ArtworkRepository;
import facul.artists.repositories.RatingRepository;
import facul.artists.repositories.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final RatingRepository ratingRepository;

    public ArtistService(ArtistRepository artistRepository, UserRepository userRepository,
            ArtworkRepository artworkRepository, RatingRepository ratingRepository) {
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public Artist createArtist(Artist artist) {

        Long userId = artist.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getArtist() != null) {
            throw new RuntimeException("Você já é um artista cadastrado.");
        }

        artist.setUser(user);
        return artistRepository.save(artist);
    }

    public Optional<Artist> getByID(Long id) {
        return artistRepository.findById(id);
    }

    @Transactional
    public Artist updateArtistAndUser(Long artistId, Map<String, Object> body) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado: " + artistId));

        User user = artist.getUser();

        if (body.containsKey("nome")) {
            user.setNome((String) body.get("nome"));
        }

        if (body.containsKey("email")) {
            user.setEmail((String) body.get("email"));
        }

        if (body.containsKey("password")) {
            String newPassword = (String) body.get("password");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setPassword(newPassword);
            }
        }

        if (body.containsKey("profilePhotoUrl")) {
            user.setProfilePhotoUrl((String) body.get("profilePhotoUrl"));
        }

        userRepository.save(user);

        if (body.containsKey("artistEmail")) {
            artist.setArtistEmail((String) body.get("artistEmail"));
        }

        if (body.containsKey("phone")) {
            artist.setPhone((String) body.get("phone"));
        }

        if (body.containsKey("city")) {
            artist.setCity((String) body.get("city"));
        }

        if (body.containsKey("biography")) {
            artist.setBiography((String) body.get("biography"));
        }

        return artistRepository.save(artist);
    }

    public void deleteArtist(Long artistId) {

        List<Artwork> artworks = artworkRepository.findByArtist_ArtistId(artistId);

        for (Artwork art : artworks) {
            ratingRepository.deleteByArtwork_ArtWorkId(art.getArtWorkId());
            artworkRepository.delete(art);
        }

        Artist artist = getByID(artistId).orElseThrow(() -> new RuntimeException("User not found"));
        artistRepository.delete(artist);
    }
}
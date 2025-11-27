package facul.artists.services;

import facul.artists.models.Artwork;
import facul.artists.models.Rating;
import facul.artists.models.User;
import facul.artists.repositories.ArtworkRepository;
import facul.artists.repositories.RatingRepository;
import facul.artists.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public Artwork addRating(Long artworkId, Long userId, int stars, String comment) {
        if (stars < 1 || stars > 5) {
            throw new RuntimeException("A avaliação deve ter entre 1 e 5 estrelas.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));

        if (ratingRepository.existsByUserIdAndArtworkArtWorkId(userId, artworkId)) {
            throw new RuntimeException("Você já avaliou esta obra.");
        }

        Rating rating = new Rating();
        rating.setArtwork(artwork);
        rating.setUser(user);
        rating.setStars(stars);
        rating.setComment(comment);
        rating.setRatingDate(LocalDate.now());

        ratingRepository.save(rating);

        Double avg = ratingRepository.getAverageRating(artworkId);
        artwork.setAverageRating(avg);

        return artworkRepository.save(artwork);
    }

    public Artwork updateRating(Long ratingId, int newStars, String newComment) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        if (newStars < 1 || newStars > 5) {
            throw new RuntimeException("Estrelas devem ser entre 1 e 5");
        }

        rating.setStars(newStars);
        rating.setComment(newComment);
        rating.setRatingDate(LocalDate.now());

        ratingRepository.save(rating);

        Long artworkId = rating.getArtwork().getArtWorkId();
        Double avg = ratingRepository.getAverageRating(artworkId);

        Artwork artwork = rating.getArtwork();
        artwork.setAverageRating(avg);

        return artworkRepository.save(artwork);
    }

    public void deleteRating(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        Long artworkId = rating.getArtwork().getArtWorkId();

        ratingRepository.deleteById(ratingId);

        Double avg = ratingRepository.getAverageRating(artworkId);

        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada"));
        artwork.setAverageRating(avg);
        artworkRepository.save(artwork);
    }

    public List<Rating> listRatings(Long artworkId) {
        return ratingRepository.findByArtworkArtWorkId(artworkId);
    }
}

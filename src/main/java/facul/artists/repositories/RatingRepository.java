package facul.artists.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import facul.artists.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT COALESCE(AVG(r.stars), 0) FROM Rating r WHERE r.artwork.artWorkId = :artworkId")
    Double getAverageRating(@Param("artworkId") Long artworkId);

    boolean existsByUserIdAndArtworkArtWorkId(Long userId, Long artworkId);

    List<Rating> findByArtworkArtWorkId(Long artworkId);

    void deleteByUserId(Long userId);

    void deleteByArtwork_ArtWorkId(Long artworkId);

}

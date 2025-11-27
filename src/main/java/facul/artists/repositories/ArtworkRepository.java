package facul.artists.repositories;

import facul.artists.models.Artwork;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByArtist_ArtistId(Long artistId);

    void deleteByArtist_ArtistId(Long artistId);

}

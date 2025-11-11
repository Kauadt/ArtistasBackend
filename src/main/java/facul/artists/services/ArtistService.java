package facul.artists.services;

import facul.artists.models.Artist;
import facul.artists.repositories.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }


    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }


    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public Optional<Artist> getById(Long id) {
        return artistRepository.findById(id);
    }

    public void delete(Long id) {
        artistRepository.deleteById(id);
    }
}
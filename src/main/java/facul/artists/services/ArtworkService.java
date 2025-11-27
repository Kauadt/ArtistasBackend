package facul.artists.services;

import facul.artists.models.Artist;
import facul.artists.models.Artwork;
import facul.artists.repositories.ArtistRepository;
import facul.artists.repositories.ArtworkRepository;
import facul.artists.repositories.RatingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ArtistRepository artistRepository;
    private final RatingRepository ratingRepository;

    private final FileStorageService fileStorageService;

    public Artwork createArtwork(
            Long artistId,
            String title,
            String description,
            String type,
            MultipartFile file) throws IOException {

        System.out.println("FILE RECEIVED = " + file.getOriginalFilename());
        System.out.println("CONTENT-TYPE = " + file.getContentType());

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado"));

        String fileName = fileStorageService.saveMedia(file);

        Artwork artwork = new Artwork();
        artwork.setArtist(artist);
        artwork.setTitle(title);
        artwork.setDescription(description);
        artwork.setType(type);

        artwork.setContentUrl("/files/" + fileName);
        artwork.setContentType(
                file.getContentType().startsWith("image") ? "image" : "video");

        return artworkRepository.save(artwork);
    }

    public Artwork getArtworkById(Long id) {
        Artwork art = artworkRepository.findById(id).orElseThrow();
        art.setAverageRating(ratingRepository.getAverageRating(art.getArtWorkId()));
        return art;
    }

    public List<Artwork> getAllArtworks() {
        List<Artwork> arts = artworkRepository.findAll();
        for (Artwork art : arts) {
            art.setAverageRating(ratingRepository.getAverageRating(art.getArtWorkId()));
        }
        return arts;
    }

    public List<Artwork> getArtworksByArtist(Long artistId) {
        List<Artwork> arts = artworkRepository.findByArtist_ArtistId(artistId);
        for (Artwork art : arts) {
            art.setAverageRating(ratingRepository.getAverageRating(art.getArtWorkId()));
        }
        return arts;
    }

    @Transactional
    public void deleteArtwork(Long id) {
        ratingRepository.deleteByArtwork_ArtWorkId(id);
        artworkRepository.deleteById(id);
    }

}

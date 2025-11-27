package facul.artists.controllers;

import facul.artists.models.Artwork;
import facul.artists.services.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/artwork")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    @PostMapping
    public ResponseEntity<?> uploadArtwork(
            @RequestParam("artistId") Long artistId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) throws IOException {

        Artwork artwork = artworkService.createArtwork(
                artistId, title, description, type, file);

        return ResponseEntity.ok(artwork);
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        return ResponseEntity.ok(artworkService.getAllArtworks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtwork(@PathVariable Long id) {
        return ResponseEntity.ok(artworkService.getArtworkById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtwork(@PathVariable Long id) {
        artworkService.deleteArtwork(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<?> listByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artworkService.getArtworksByArtist(artistId));
    }

}

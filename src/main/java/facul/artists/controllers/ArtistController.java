package facul.artists.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import facul.artists.models.Artist;
import facul.artists.services.ArtistService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/artist")
public class ArtistController {

    private ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<Artist> listAll() {
        return artistService.getAll();
    }

    @PostMapping
    public Artist save(@RequestBody Artist artist) {
        return artistService.createArtist(artist);
    }

    @PutMapping("/{artistId}")
    public Artist updateArtistAndUser(
            @PathVariable Long artistId,
            @RequestBody Map<String, Object> body) {

        return artistService.updateArtistAndUser(artistId, body);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        artistService.deleteArtist(id);
    }

    @GetMapping("/{id}")
    public Optional<Artist> findById(@PathVariable Long id) {
        return artistService.getByID(id);
    }
}

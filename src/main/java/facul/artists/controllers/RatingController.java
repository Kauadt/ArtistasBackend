package facul.artists.controllers;

import facul.artists.models.Artwork;
import facul.artists.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rating")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/{artworkId}")
    public ResponseEntity<?> rateArtwork(
            @PathVariable Long artworkId,
            @RequestParam Long userId,
            @RequestParam int stars,
            @RequestParam(required = false) String comment) {

        Artwork artwork = ratingService.addRating(artworkId, userId, stars, comment);
        return ResponseEntity.ok(artwork);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<?> update(
            @PathVariable Long ratingId,
            @RequestParam int stars,
            @RequestParam(required = false) String comment) {

        Artwork artwork = ratingService.updateRating(ratingId, stars, comment);
        return ResponseEntity.ok(artwork);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> delete(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.ok("Avaliação removida.");
    }

    @GetMapping("/{artworkId}")
    public ResponseEntity<?> getRatings(@PathVariable Long artworkId) {
        return ResponseEntity.ok(ratingService.listRatings(artworkId));
    }
}

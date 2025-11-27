package facul.artists.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artWorkId;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String contentUrl;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private LocalDate publishDate = LocalDate.now();

    @Transient
    private Double averageRating;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Rating> ratings = new ArrayList<>();
}

package facul.artists.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long artistId;

    @Column(nullable = false)
    private String artistEmail;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String city;

    private String biography;

    @OneToOne
    @JoinColumn(name = "id", unique = true, nullable = false)
    private User user;
}
package recordstore.entity;

import javafx.beans.DefaultProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "releases", schema = "recordstore")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    @NotBlank(message = "Field is mandatory")
    private String title;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "format")
    @NotBlank(message = "Field is mandatory")
    private String format;

    @Column(name = "price")
    private double price;

    @Column(name = "img", nullable = true)
    private String img = "noImageAvailable.png";

    @Transient
    private MultipartFile data;

    @Column(name = "quantity")
    private int quantity;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "releases_artists",
            joinColumns = @JoinColumn(name = "release_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "releases_genres",
            joinColumns = @JoinColumn(name = "release_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Label label;

    public void addArtist(Artist artist){
        this.artists.add(artist);
        artist.getReleases().add(this);
    }
    public void removeArtist(Artist artist){
        this.artists.remove(artist);
        artist.getReleases().remove(this);
    }

    public void addGenre(Genre genre){
        this.genres.add(genre);
        genre.getReleases().add(this);
    }
    public void removeGenre(Genre genre){
        this.genres.remove(genre);
        genre.getReleases().remove(this);
    }

    public void addLabel(Label label){
        this.setLabel(label);
        label.getReleases().add(this);
    }
    public void removeLabel(Genre genre){
        this.setLabel(null);
        label.getReleases().remove(this);
    }
}
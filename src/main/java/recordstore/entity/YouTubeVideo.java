package recordstore.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "video", schema = "recordstore")
public class YouTubeVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "videoId")
    String videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Release release;
}
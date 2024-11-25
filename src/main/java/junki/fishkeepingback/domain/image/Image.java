package junki.fishkeepingback.domain.image;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.post.Post;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String originalFilename;
    private String fileName;
    private String url;

    public Image(Post post, String originalFilename, String fileName, String url) {
        this.post = post;
        this.originalFilename = originalFilename;
        this.fileName = fileName;
        this.url = url;
    }
}

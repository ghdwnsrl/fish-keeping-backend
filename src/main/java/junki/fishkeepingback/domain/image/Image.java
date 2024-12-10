package junki.fishkeepingback.domain.image;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.post.Post;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private String fileName;
    private String url;

    public Image(Post post, String url, String fileName) {
        this.post = post;
        this.url = url;
        this.fileName = fileName;
    }
}

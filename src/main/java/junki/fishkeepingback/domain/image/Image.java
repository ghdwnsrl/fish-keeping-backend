package junki.fishkeepingback.domain.image;

import jakarta.persistence.*;
import junki.fishkeepingback.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @Enumerated(EnumType.STRING)
    private ImageType type;

    public Image(Post post, String url, String fileName, ImageType type) {
        this.post = post;
        this.url = url;
        this.fileName = fileName;
        this.type = type;
    }
}

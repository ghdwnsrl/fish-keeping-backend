package junki.fishkeepingback.domain.archive.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.archive.Archive;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.image.Image;
import junki.fishkeepingback.domain.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Comparator.*;
import static junki.fishkeepingback.domain.archive.QArchive.*;
import static junki.fishkeepingback.domain.image.ImageType.*;
import static junki.fishkeepingback.domain.image.QImage.image;
import static junki.fishkeepingback.domain.post.QPost.post;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomArchiveRepositoryImpl implements CustomArchiveRepository {

    private final JPAQueryFactory query;

    // archive 별로 그룹을 나누고 최신에 작성된 post의 image의 url을 가져와야한다. 이때 imageType은 THUMBNAIL이다
    // archive의 최신 post를 가져오고
    @Override
    public List<ArchiveRes> findByUsername(String username) {
        List<Archive> archives = query
                .selectFrom(archive)
                .leftJoin(post).on(archive.id.eq(post.archive.id))
                .leftJoin(image).on(post.id.eq(image.post.id))
                .where(archive.user.username.eq(username))
                .fetch();

        return archives.stream().map(archive -> {
            Post latestPost = archive.getPosts().stream()
                    .max(comparing(Post::getCreatedAt))  // createdAt 기준으로 최신 글을 찾기
                    .orElse(null);
            if (latestPost == null) {
                return new ArchiveRes(archive, null, 0L, null);
            }
            String thumbnailUrl = latestPost.getImages().stream()
                    .filter(i -> i.getType().equals(THUMBNAIL))
                    .findFirst()
                    .map(Image::getUrl)
                    .orElse(null);
            return new ArchiveRes(archive, thumbnailUrl, Integer.toUnsignedLong(archive.getPosts().size()), latestPost.getUpdatedAt());
        }).toList();

    }

    @Override
    public boolean existsByUsernameAndArchiveName(String username, String archiveName) {
        return query.selectOne()
                .from(archive)
                .where(archive.user.username.eq(username).and(archive.name.eq(archiveName)))
                .fetchFirst() != null;
    }
}
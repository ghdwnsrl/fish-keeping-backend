package junki.fishkeepingback.domain.archive.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.post.QPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
        QPost latestPost = new QPost("latestPost");
        QPost countingPost = new QPost("countingPost");
        return query.select(Projections.constructor(
                        ArchiveRes.class,
                        archive.id,
                        image.url,
                        archive.name,
                        JPAExpressions
                                .select(countingPost.count())
                                .from(countingPost)
                                .where(countingPost.archive.id.eq(archive.id)),
                        post.createdAt
                ))
                .from(archive)
                .leftJoin(post).on(post.createdAt.eq(
                        JPAExpressions
                                .select(latestPost.createdAt.max())
                                .from(latestPost)
                                .where(latestPost.archive.id.eq(archive.id))
                ))
                .leftJoin(image).on(image.post.id.eq(post.id).and(image.type.eq(THUMBNAIL)))
                .where(archive.user.username.eq(username))
                .fetch();
    }

    @Override
    public boolean existsByUsernameAndArchiveName(String username, String archiveName) {
        return query.selectOne()
                .from(archive)
                .where(archive.user.username.eq(username).and(archive.name.eq(archiveName)))
                .fetchFirst() != null;
    }
}
package junki.fishkeepingback.domain.archive.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
import junki.fishkeepingback.domain.post.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static junki.fishkeepingback.domain.archive.QArchive.*;
import static junki.fishkeepingback.domain.post.QPost.*;

@Component
@RequiredArgsConstructor
public class CustomArchiveRepositoryImpl implements CustomArchiveRepository {

    private final JPAQueryFactory query;

    @Override
    public List<ArchiveRes> findByUsername(String username) {
        QPost latestPost = new QPost("latestPost");

        return query.select(Projections.constructor(
                        ArchiveRes.class,
                        archive.id.as("id"),
                        JPAExpressions
                                .select(latestPost.thumbnailUrl)
                                .from(latestPost)
                                .where(latestPost.archive.id.eq(archive.id)
                                        .and(latestPost.createdAt.eq(post.createdAt.max()))),
                        archive.name.as("name"),
                        post.id.count().as("totalPosts"),
                        post.createdAt.max().as("lastModified")
                ))
                .from(archive)
                .leftJoin(post).on(archive.id.eq(post.archive.id))
                .where(archive.user.username.eq(username))
                .groupBy(archive.id)
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

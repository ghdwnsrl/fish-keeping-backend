package junki.fishkeepingback.domain.archive.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.archive.dto.ArchiveRes;
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
        return query.select(Projections.constructor(
                        ArchiveRes.class,
                        archive.id.as("id"),
                        archive.name.as("name"),
                        post.id.count().as("totalPosts"),
                        post.createdAt.max().as("lastModified")
                ))
                .from(post).leftJoin(post.archive, archive)
                .where(post.user.username.eq(username))
                .groupBy(post.archive.id)
                .fetch();
    }
}

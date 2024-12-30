package junki.fishkeepingback.domain.post.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.dto.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static junki.fishkeepingback.domain.post.QPost.post;
import static junki.fishkeepingback.domain.postlike.QPostLike.*;

@Component
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostRes> findByUsername(String username, String archiveName, Pageable pageable) {

        List<PostRes> contents = queryFactory.select(
                        Projections.constructor(
                                PostRes.class,
                                post.id,
                                post.title,
                                post.user.username,
                                post.comments.size(),
                                post.views,
                                post.thumbnailUrl,
                                JPAExpressions
                                        .select(postLike.count())
                                        .from(postLike)
                                        .where(postLike.post.id.eq(post.id)),
                                post.createdAt
                        ))
                .from(post)
                .where(usernameEq(username), archiveNameEq(archiveName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(usernameEq(username));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public List<PostRes> findByTop3() {

        return queryFactory.select(Projections.constructor(
                PostRes.class,
                post.id,
                post.title,
                post.user.username,
                post.comments.size(),
                post.views,
                post.thumbnailUrl,
                JPAExpressions
                        .select(postLike.count())
                        .from(postLike)
                        .where(postLike.post.id.eq(post.id)),
                post.createdAt
                ))
                .from(post)
                .leftJoin(postLike).on(postLike.post.id.eq(post.id))
                .groupBy(post.id)
                .orderBy(postLike.count().desc())
                .limit(3)
                .fetch();
    }

    private BooleanExpression usernameEq(String nameCond) {
        if (nameCond == null) { return null; }
        return post.user.username.eq(nameCond);
    }

    private BooleanExpression archiveNameEq(String nameCond) {
        if (nameCond == null) { return null; }
        return post.archive.name.eq(nameCond);
    }
}
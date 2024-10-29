package junki.fishkeepingback.domain.post.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.post.Post;
import junki.fishkeepingback.domain.post.QPost;
import junki.fishkeepingback.domain.post.dto.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostRes> findByUsername(String username, Pageable pageable) {

        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        Optional.ofNullable(username)
                        .ifPresent(u -> builder.and(post.user.username.eq(u)));

        List<PostRes> contents = queryFactory.select(
                        Projections.constructor(
                                PostRes.class,
                                post.id,
                                post.title,
                                post.user.username,
                                post.views,
                                post.createdAt
                        ))
                .from(post)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(post.count())
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(contents, pageable, count);
    }
}

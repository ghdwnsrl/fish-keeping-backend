package junki.fishkeepingback.domain.comment.dao;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import junki.fishkeepingback.domain.comment.dto.CommentDto;
import junki.fishkeepingback.domain.comment.dto.CommentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static junki.fishkeepingback.domain.comment.QComment.*;
import static junki.fishkeepingback.domain.comment.QCommentPathView.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentRes> findCommentsByPostId(Long postId, Pageable pageable) {

        Map<Long, List<CommentDto>> transform = queryFactory
                .selectFrom(commentPathView)
                .where(commentPathView.postId.eq(postId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(commentPathView.groupId.asc(), commentPathView.createdAt.asc())
                .transform(GroupBy.groupBy(commentPathView.groupId)
                        .as(GroupBy.list(Projections.constructor(
                                        CommentDto.class,
                                        commentPathView.commentId,
                                        commentPathView.content,
                                        commentPathView.username,
                                        commentPathView.profileImageUrl,
                                        commentPathView.createdAt
                                ))
                        ));

        List<CommentRes> result = getResult(transform);

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private static List<CommentRes> getResult(Map<Long, List<CommentDto>> transform) {
        List<CommentRes> result = new ArrayList<>();
        transform.forEach((groupId, commentDtos) ->
                commentDtos.stream()
                        .filter(commentDto -> commentDto.id().equals(groupId))
                        .findFirst()
                        .ifPresentOrElse((parentComment) -> {

                                    List<CommentDto> childComments = commentDtos.stream()
                                            .filter(commentDto -> !commentDto.id().equals(groupId))
                                            .collect(Collectors.toList());

                                    CommentRes commentRes = new CommentRes(
                                            parentComment.id(),
                                            parentComment.content(),
                                            parentComment.username(),
                                            parentComment.profileImageUrl(),
                                            childComments,
                                            parentComment.createdAt()
                                    );
                                    result.add(commentRes);
                                },
                                () -> {
                                    CommentRes commentRes = new CommentRes(
                                            null,
                                            null,
                                            null,
                                            null,
                                            commentDtos,
                                            null
                                    );
                                    result.add(commentRes);
                                }));
        return result;
    }

}

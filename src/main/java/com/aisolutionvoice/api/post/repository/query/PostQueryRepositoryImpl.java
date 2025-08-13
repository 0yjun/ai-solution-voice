package com.aisolutionvoice.api.post.repository.query;

import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.entity.QMember;
import com.aisolutionvoice.api.post.dto.PostSearchRequestDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Page<PostSummaryDto> search(PostSearchRequestDto cond, Pageable pageable) {
        QPost post = QPost.post;
        QMember member = QMember.member;
        BooleanBuilder where = new BooleanBuilder();

        LocalDate from = cond.getCreatedFrom();
        LocalDate to   = cond.getCreatedTo();

        LocalDateTime fromDt = (from != null) ? from.atStartOfDay() : null;
        LocalDateTime toDt = (to != null) ? to.atStartOfDay() : null;

        if (cond.getBoardId() != null) where.and(post.board.id.eq(cond.getBoardId()));
        if (cond.getTitle() != null && !cond.getTitle().isBlank()) where.and(post.title.containsIgnoreCase(cond.getTitle()));
        if (cond.getIsChecked() != null) where.and(post.isChecked.eq(cond.getIsChecked()));
        if (fromDt != null) where.and(post.createdAt.goe(fromDt));
        if (toDt != null) where.and(post.createdAt.lt(toDt));

        List<PostSummaryDto> content = query
                .select(Projections.constructor(
                        PostSummaryDto.class, post.id, post.title, member.loginId, post.createdAt, post.isChecked)
                )
                .from(post)
                .join(post.member, member)
                .where(where)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(post.count()).from(post).where(where).fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
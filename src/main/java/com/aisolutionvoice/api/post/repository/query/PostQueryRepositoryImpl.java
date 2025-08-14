package com.aisolutionvoice.api.post.repository.query;

import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.entity.QHotwordScript;
import com.aisolutionvoice.api.member.entity.QMember;
import com.aisolutionvoice.api.post.dto.PostFlatRowDto;
import com.aisolutionvoice.api.post.dto.PostSearchRequestDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.entity.Post;
import com.aisolutionvoice.api.post.entity.QPost;
import com.aisolutionvoice.api.post.repository.query.PostQueryRepository;
import com.aisolutionvoice.api.voiceData.entity.QVoiceData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {
    private final JPAQueryFactory query;

    private static final QPost POST = QPost.post;
    private static final QMember MEMBER = QMember.member;
    private static final QHotwordScript HOTWORD_SCRIPT = QHotwordScript.hotwordScript;
    private static final QVoiceData VOICE_DATA = QVoiceData.voiceData;

    private BooleanBuilder getCondition(PostSearchRequestDto cond, Integer memberId){
        BooleanBuilder where = new BooleanBuilder();

        LocalDate from = cond.getCreatedFrom();
        LocalDate to   = cond.getCreatedTo();

        LocalDateTime fromDt = (from != null) ? from.atStartOfDay() : null;
        LocalDateTime toDt = (to != null) ? to.atStartOfDay() : null;

        if (cond.getBoardId() != null) where.and(POST.board.id.eq(cond.getBoardId()));
        if (StringUtils.hasText(cond.getTitle())) where.and(POST.title.containsIgnoreCase(cond.getTitle()));
        if (cond.getIsChecked() != null) where.and(POST.isChecked.eq(cond.getIsChecked()));
        if (fromDt != null) where.and(POST.createdAt.goe(fromDt));
        if (toDt != null) where.and(POST.createdAt.lt(toDt));
        if(memberId != null) where.and(MEMBER.memberId.eq(memberId));

        return where;
    }

    @Override
    public Page<PostSummaryDto> search(PostSearchRequestDto cond, Integer memberId, Pageable pageable) {
        BooleanBuilder where = getCondition(cond, memberId);

        List<PostSummaryDto> content = query
                .select(Projections.constructor(
                        PostSummaryDto.class, POST.id, POST.title, MEMBER.loginId, POST.createdAt, POST.isChecked)
                )
                .from(POST)
                .join(POST.member, MEMBER)
                .where(where)
                .orderBy(POST.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(POST.count().coalesce(0L))
                .from(POST)
                .join(POST.member, MEMBER)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, Objects.requireNonNullElse(total, 0L));
    }

    @Override
    public Long countByCheckedTrueAndCond(PostSearchRequestDto cond, Integer memberId) {
        BooleanBuilder where = getCondition(cond, memberId);
        return query
                .select(POST.count().coalesce(0L))
                .from(POST)
                .join(POST.member, MEMBER)
                .where(where, POST.isChecked.isTrue())
                .fetchOne();
    }

    @Override
    public Optional<Long> findPostIdByMemberIdAndBoardId(Integer memberId, Long boardId) {
        Long id = query
                .select(POST.id)
                .from(POST)
                .where(POST.member.memberId.eq(memberId),
                        POST.board.id.eq(boardId))
                .limit(1)              // 여러 건 가능성 있으면 안전하게 1건
                .fetchFirst();         // fetchOne()은 다건이면 예외
        return Optional.ofNullable(id);
    }

//    @Override
//    public List<PostFlatRowDto> findPostFlatRows(Long postId) {
//        return query
//                .select(Projections.constructor(
//                        PostFlatRowDto.class,
//                        POST.id,
//                        POST.title,
//                        HOTWORD_SCRIPT.scriptId,
//                        HOTWORD_SCRIPT.text,
//                        VOICE_DATA.id,
//                        POST.memo,
//                        POST.isChecked
//                ))
//                .from(POST)
//                .join(HOTWORD_SCRIPT).on(HOTWORD_SCRIPT.board.id.eq(POST.board.id))
//                .leftJoin(VOICE_DATA).on(
//                        VOICE_DATA.id.eq(POST.id)
//                                .and(VOICE_DATA.hotwordScript.scriptId.eq(HOTWORD_SCRIPT.scriptId))
//                )
//                .where(POST.id.eq(postId))
//                .orderBy(HOTWORD_SCRIPT.scriptId.asc())
//                .fetch();
//    }


}
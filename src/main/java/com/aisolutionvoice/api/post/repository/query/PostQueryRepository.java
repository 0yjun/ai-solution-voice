package com.aisolutionvoice.api.post.repository.query;

import com.aisolutionvoice.api.Board.entity.Board;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.post.dto.PostFlatRowDto;
import com.aisolutionvoice.api.post.dto.PostSearchRequestDto;
import com.aisolutionvoice.api.post.dto.PostSummaryDto;
import com.aisolutionvoice.api.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostQueryRepository {
    Page<PostSummaryDto> search(PostSearchRequestDto condition, Pageable pageable);
}

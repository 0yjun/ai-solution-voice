package com.aisolutionvoice.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Schema(description = "게시글 목록 제공 DTO")
@Getter
@Setter
@NoArgsConstructor
public class PostSummaryDto {
    @Schema(description = "로그인 ID", example = "admin1")
    @NotBlank
    private Long postId;

    @Schema(description = "게시글 제목", example = "게시글")
    @NotBlank
    private String title;

    @Schema(description = "작성자 로그인아이디 ", example = "securePass123!")
    @NotBlank
    private String writerLoginId;

    @Schema(description = "작성일자 ", example = "2025-01-01")
    @NotBlank
    private String createAt;

    @Schema(description = "체크여부", example = "true/false")
    private boolean isChecked;

    // JPQL 프로젝션을 위한 생성자
    public PostSummaryDto(Long postId, String title, String loginId, LocalDateTime createAt, boolean isChecked) {
        this.postId = postId;
        this.title = title;
        this.writerLoginId = loginId;
        this.createAt = createAt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.isChecked = isChecked;
    }

    public PostSummaryDto applyDefaultTitle() {
        if (!StringUtils.hasText(this.title)) {
            this.title = this.writerLoginId + "님이 작성한 게시글";
        }
        return this;
    }
}
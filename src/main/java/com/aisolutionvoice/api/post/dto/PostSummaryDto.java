package com.aisolutionvoice.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "게시글 목록 제공 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
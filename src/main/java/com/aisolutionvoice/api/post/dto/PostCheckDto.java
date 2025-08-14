package com.aisolutionvoice.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PostCheckDto {
    @Schema(description = "존재여부", example = "true/false")
    private Boolean isExist;

    @Schema(description = "게시글아이디", example = "1")
    private Long postId;
}

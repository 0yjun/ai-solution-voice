package com.aisolutionvoice.api.post.dto;

import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.voiceData.dto.VoiceDataDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Schema(description = "게시글 상세보기 제공 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchRequestDto {
    @Schema(description = "", example = "1")
    @NotBlank
    private Long boardId;

    @Schema(description = "검색어", example = "admin1")
    private String title;

    @Schema(description = "체크여부", example = "admin1")
    private Boolean isChecked;

    @Schema(description = "생성시작일자", example = "2020-01-01")
    private String createdStart;

    @Schema(description = "생성종료일자", example = "2025-05-12")
    private String createdEnd;
}
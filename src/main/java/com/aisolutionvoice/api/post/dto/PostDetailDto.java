package com.aisolutionvoice.api.post.dto;

import com.aisolutionvoice.api.HotwordScript.dto.HotwordScriptDto;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.voiceData.dto.VoiceDataDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "게시글 상세보기 제공 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailDto {
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

    @Schema(description = "음성데이터 리스트")
    @NotBlank
    private List<VoiceDataDto> voiceDataDtoList;

    @Schema(description = "개인정보 메시지", example = "계좌번호: .....")
    private String memo;

    @Schema(description = "체크여부", example = "true/false")
    private boolean isChecked;

    @Schema(description = "음성데이터 리스트")
    @NotBlank
    private List<HotwordScriptDto> scripts;

    public PostDetailDto(Long postId, String title, String memo, boolean isChecked, List<HotwordScriptDto> scripts) {
        this.postId = postId;
        this.title = title;
        this.memo = memo;
        this.isChecked = isChecked;
        this.scripts = scripts;
    }

    public static PostDetailDto fromFlatRows(List<PostFlatRowDto> rows) {
        if (rows.isEmpty()) return null;
        PostFlatRowDto first = rows.get(0);
        List<HotwordScriptDto> scripts = rows.stream()
                .map(r -> new HotwordScriptDto(r.getHotwordScriptId(), r.getHotwordText(), r.getVoiceDataUrl()))
                .toList();
        return new PostDetailDto(first.getPostId(), first.getTitle(), first.getMemo(), first.isChecked(), scripts);
    }
}
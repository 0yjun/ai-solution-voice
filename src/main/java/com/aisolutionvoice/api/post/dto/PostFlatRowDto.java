package com.aisolutionvoice.api.post.dto;

import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.voiceData.dto.VoiceDataDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Schema(description = "게시글 상세보기 제공 DTO")
@Data
public class PostFlatRowDto {
    private static final String BASE_VOICE_DATA_API_PATH = "/api/voice-data/"; // 상수로 관리
    public PostFlatRowDto(Long postId, String title, Long hotwordScriptId, String hotwordText, Long voiceDataId,
                          String memo, boolean isChecked) {
        this.postId = postId;
        this.title = title;
        this.hotwordScriptId = hotwordScriptId;
        this.hotwordText = hotwordText;
        this.voiceDataUrl = (voiceDataId != null)
                ? BASE_VOICE_DATA_API_PATH + voiceDataId
                : null;
        this.memo = memo;
        this.isChecked = isChecked;
    }

    @Schema(description = "로그인 ID", example = "admin1")
    @NotBlank
    private Long postId;

    @Schema(description = "게시글 제목", example = "게시글")
    @NotBlank
    private String title;

    @Schema(description = "호출어 아이디", example = "1")
    private Long hotwordScriptId;

    @Schema(description = "호출어 음성 ", example = "안녕지니야")
    private String hotwordText;

    @Schema(description = "음성데이터 아이디", example = "/api/voice-data/1")
    private String voiceDataUrl;

    @Schema(description = "게시글 메모", example = "1234")
    private String memo;

    @Schema(description = "체크여부", example = "true/false")
    private boolean isChecked;

    public PostFlatRowDto(){

    }
}
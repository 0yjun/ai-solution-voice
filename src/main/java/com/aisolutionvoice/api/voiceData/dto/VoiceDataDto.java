package com.aisolutionvoice.api.voiceData.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "음성데이터 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoiceDataDto {
    @Schema(description = "로그인 ID", example = "admin1")
    @NotBlank
    private Long voiceDataDto;

    @Schema(description = "음성데이터 주소", example = "/home/gosh2")
    @NotBlank
    private String audioFilePath;

    @Schema(description = "재생기간 ", example = "123.123")
    @NotBlank
    private Double duration;

    @Schema(description = "생성시간 ", example = "2025.01.01")
    @NotBlank
    private LocalDateTime submittedAt;

    @Schema(description = "호출어 아이디", example = "1")
    @NotBlank
    private String hotwordScriptId;

    @Schema(description = "호출어 텍스트", example = "지니야")
    @NotBlank
    private String scriptText;

}

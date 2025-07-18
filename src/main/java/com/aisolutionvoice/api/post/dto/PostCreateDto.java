package com.aisolutionvoice.api.post.dto;

import com.aisolutionvoice.api.voiceData.dto.VoiceDataDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "게시글 생성 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {
    @Schema(description = "개인정보 메시지", example = "계좌번호: .....")
    @NotBlank
    private String memo;
}

package com.aisolutionvoice.api.Board.dto;

import com.aisolutionvoice.api.Board.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "AdminBoardDto", description = "관리자용 게시판 목록 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminBoardDto {

    @Schema(description = "게시판 ID", example = "42")
    private Long boardId;

    @Schema(description = "게시판 명", example = "차량 호출어 수집")
    private String boardName;

    @Schema(description = "게시판 설명", example = "운전 중 사용할 호출어를 녹음하세요")
    private String description;

    @Schema(description = "활성화 여부", example = "true")
    private boolean activated;

    @Schema(description = "생성일")
    private LocalDateTime createdAt;

    @Schema(description = "최종 수정일")
    private LocalDateTime updatedAt;

    @Schema(description = "스크립트 수정 가능 여부", example = "true")
    private boolean canEditScript;

    public static AdminBoardDto fromEntity(Board board) {
        return new AdminBoardDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                !board.isDeleted(), // 'deleted'의 반대 값으로 'activated' 설정
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.getPosts().isEmpty() // canEditScript 설정
        );
    }
}

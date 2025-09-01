package com.aisolutionvoice.api.notice.dto;

import com.aisolutionvoice.api.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListResponseDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;

    public static NoticeListResponseDto from(Notice notice) {
        return new NoticeListResponseDto(
                notice.getId(),
                notice.getTitle(),
                notice.getCreatedAt()
        );
    }
}

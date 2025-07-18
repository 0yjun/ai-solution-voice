package com.aisolutionvoice.common.file;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FilePathGenerator {
    private static final String VOICE_DATA_PATH_TEMPLATE =
            "voice" + File.separator + "board%d" + File.separator + "post%d" + File.separator + "%d.wav";

    /**
     * 음성 데이터 파일의 상대 경로를 생성합니다.
     *
     * @param boardId 게시판 ID
     * @param postId  게시글 ID
     * @param hotwordId 호출어 스크립트 ID
     * @return 생성된 상대 파일 경로 (예: "voice/board1/post42/101.wav")
     */
    public String generateVoiceDataPath(Long boardId, Long postId, Long hotwordId) {
        // (2) String.format을 사용하여 템플릿에 값을 매핑
        return String.format(VOICE_DATA_PATH_TEMPLATE, boardId, postId, hotwordId);
    }

}

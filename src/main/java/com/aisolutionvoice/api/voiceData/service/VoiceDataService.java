package com.aisolutionvoice.api.voiceData.service;

import com.aisolutionvoice.api.Board.service.BoardService;
import com.aisolutionvoice.api.HotwordScript.entity.HotwordScript;
import com.aisolutionvoice.api.HotwordScript.repository.HotwordScriptRepository;
import com.aisolutionvoice.api.HotwordScript.service.HotwordScriptService;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.post.entity.Post;
import com.aisolutionvoice.api.post.repository.PostRepository;
import com.aisolutionvoice.api.voiceData.entity.VoiceData;
import com.aisolutionvoice.api.voiceData.repository.VoiceDataRepository;
import com.aisolutionvoice.common.file.FilePathGenerator;
import com.aisolutionvoice.common.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceDataService {
    private final HotwordScriptService hotwordScriptService;
    private final FilePathGenerator filePathGenerator;
    private final FileStorageService fileStorageService;
    private final String VOICE_PREFIX = "voice_";

    @Transactional
    public void saveVoiceDataList(Post post, Map<String, MultipartFile> files) {
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            if (!entry.getKey().startsWith(VOICE_PREFIX)) continue;

            Long scriptId = Long.valueOf(entry.getKey().substring(VOICE_PREFIX.length()));
            HotwordScript script = hotwordScriptService.getScriptByProxy(scriptId);

            String relativePath =
                    filePathGenerator.generateVoiceDataPath(post.getBoard().getId(), post.getId(), scriptId);

            fileStorageService.store(entry.getValue(), relativePath);


            VoiceData voiceData = VoiceData.builder()
                    .audioFilePath(relativePath)
                    .hotwordScript(script)
                    .build();

            post.addVoiceData(voiceData); // 연관관계 유지
        }
    }

}

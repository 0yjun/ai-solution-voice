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
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceDataService {
    private final HotwordScriptService hotwordScriptService;
    private final FilePathGenerator filePathGenerator;
    private final FileStorageService fileStorageService;
    private final VoiceDataRepository voiceDataRepository;
    private final String VOICE_PREFIX = "voice_";

    public Resource getAudioResourceById(Long voiceDataId){
        String filePath = getPathById(voiceDataId);
        return fileStorageService.loadAudioByPath(filePath);
    }

    private String getPathById(Long voiceDataId){
        return voiceDataRepository.findById(voiceDataId).orElseThrow(
                ()-> new CustomException(ErrorCode.RESOURCE_NOT_FOUND)
        ).getAudioFilePath();
    }

    @Transactional
    public void createVoiceDataList(Post post, Map<String, MultipartFile> files) {
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            if (!entry.getKey().startsWith(VOICE_PREFIX)) continue;

            Long scriptId = Long.valueOf(entry.getKey().substring(VOICE_PREFIX.length()));
            HotwordScript script = hotwordScriptService.getScriptByProxy(scriptId);

            String relativePath =
                    filePathGenerator.generateVoiceDataPath(post.getBoard().getId(), post.getId(), scriptId);

            VoiceData voiceData = VoiceData.builder()
                    .audioFilePath(relativePath)
                    .hotwordScript(script)
                    .build();

            post.addVoiceData(voiceData); // 연관관계 유지
            fileStorageService.store(entry.getValue(), relativePath);
        }
    }

    @Transactional
    public void updateVoiceDataList(Post post, Map<String, MultipartFile> files) {
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            if (!entry.getKey().startsWith(VOICE_PREFIX)) continue;

            Long scriptId = Long.valueOf(entry.getKey().substring(VOICE_PREFIX.length()));
            HotwordScript script = hotwordScriptService.getScriptByProxy(scriptId);

            String relativePath =
                    filePathGenerator.generateVoiceDataPath(post.getBoard().getId(), post.getId(), scriptId);
            log.info("script.getScriptId()"+script.getScriptId());
            voiceDataRepository.findAll().stream().forEach(i->{
                log.info(i.toString());
                log.info(i.getId().toString());
            });
            VoiceData voiceData = voiceDataRepository.findById(script.getScriptId())
                    .orElseThrow(()->new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

            //post.updateVoiceData(voiceData); // 연관관계 유지
            fileStorageService.store(entry.getValue(), relativePath);
        }
    }

}

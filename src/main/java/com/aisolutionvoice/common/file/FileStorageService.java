package com.aisolutionvoice.common.file;

import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.storage.base-path}") // application.yml 등에서 기본 경로 주입
    private String baseStoragePath;

    /**
     * 주어진 상대경로의 음성 파일을 Resource로 반환
     * @param filePath 상대 경로 (예: "voice/board1/post42/101.wav")
     * @return Resource 객체 (실제 파일)
     * @throws java.io.FileNotFoundException 존재하지 않을 경우
     */
    public Resource loadAudioByPath(String filePath) {
        try {
            Path fullPath = Paths.get(this.baseStoragePath).resolve(filePath).normalize();
            Resource resource = new UrlResource(fullPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new CustomException(ErrorCode.INVALID_FILE_PATH);
            }
        } catch (MalformedURLException ex) {
            throw new CustomException(ErrorCode.INVALID_FILE_PATH);
        }
    }

    /**
     * 파일을 지정된 상대 경로에 저장합니다.
     *
     * @param file         저장할 MultipartFile
     * @param relativePath 저장될 파일의 상대 경로 (디렉토리 포함)
     */
    public void store(MultipartFile file, String relativePath) {
        try {
            if (file.isEmpty()) {
                throw new CustomException(ErrorCode.FILE_EMPTY_ERROR);
            }

            // 1. 최종 절대 경로 계산
            Path targetPath = Paths.get(this.baseStoragePath).resolve(relativePath).normalize();

            // 2. 부모 디렉토리 생성 (없을 경우)
            Files.createDirectories(targetPath.getParent());

            // 3. 파일 저장 (StandardCopyOption.REPLACE_EXISTING으로 덮어쓰기 보장)
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SAVE_ERROR);
        }
    }


}

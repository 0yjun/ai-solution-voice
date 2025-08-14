package com.aisolutionvoice.api.HotwordScript.controller;

import com.aisolutionvoice.api.HotwordScript.service.HotwordScriptService;
import com.aisolutionvoice.api.post.dto.PostCreateDto;
import com.aisolutionvoice.security.model.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotword-script")
@RequiredArgsConstructor
@Slf4j
public class HotwordScriptController {
    private final HotwordScriptService hotwordScriptService;

    public ResponseEntity<?> getList(@RequestParam String text, Pageable pageable){
        Page result = hotwordScriptService.getPage(text, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody String text) {
        Long scriptId = hotwordScriptService.save(text);
        URI location = URI.create("/api/hotword-script/" + scriptId);
        return ResponseEntity.created(location)
                .build();
    }
}

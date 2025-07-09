package com.aisolutionvoice.api.auth.controller;

import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 및 사용자 관리 API")
@Slf4j
public class AuthController {
    @PostMapping("/signup")
    @Operation(summary = "회원 등록", description = "새로운 관리자를 등록합니다.")
    public ResponseEntity<Object> createMember(@RequestBody @Valid SignupRequestDto request) {
        Object data = new Object();

        return ResponseEntity.ok(request);
    }
}

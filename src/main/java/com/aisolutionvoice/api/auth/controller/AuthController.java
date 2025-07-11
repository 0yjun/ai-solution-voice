package com.aisolutionvoice.api.auth.controller;

import com.aisolutionvoice.api.auth.dto.LoginRequestDto;
import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
import com.aisolutionvoice.api.auth.service.AuthService;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 및 사용자 관리 API")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    @PostMapping("/signup")
    @Operation(summary = "회원 등록", description = "새로운 관리자를 등록합니다.")
    public ResponseEntity<Object> createMember(@RequestBody @Valid SignupRequestDto request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(token); // ⬅️ 이때 MemberDetailsService 호출됨

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("로그인 성공");
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // authentication.getPrincipal()은 UserDetails를 반환
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 필요한 정보만 추려서 DTO로 반환
        return ResponseEntity.ok(userDetails);
    }
}

package com.aisolutionvoice.api.auth.controller;

import com.aisolutionvoice.api.auth.dto.LoginRequestDto;
import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
import com.aisolutionvoice.api.auth.service.AuthService;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
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
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/signup")
    @Operation(summary = "회원 등록", description = "새로운 관리자를 등록합니다.")
    public ResponseEntity<Object> createMember(@RequestBody @Valid SignupRequestDto request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());
        System.out.println(token);
        Authentication auth = authenticationManager.authenticate(token); // ⬅️ 이때 MemberDetailsService 호출됨
        System.out.println(auth);
        // SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        securityContextRepository.saveContext(context, request, response);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        System.out.println(userDetails);

        return ResponseEntity.ok(userDetails);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // 현재 세션 조회
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        SecurityContextHolder.clearContext(); // SecurityContext도 비움

        return ResponseEntity.ok(Map.of("message", "로그아웃되었습니다"));
    }

    @PreAuthorize("hasRole('User')")
    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.AUTH_NOT_AUTHENTICATED);
        }

        // authentication.getPrincipal()은 UserDetails를 반환
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 필요한 정보만 추려서 DTO로 반환
        return ResponseEntity.ok(Map.of(
                "loginId", userDetails.getUsername(),
                "role", userDetails.getAuthorities().iterator().next().getAuthority()
        ));
    }
}

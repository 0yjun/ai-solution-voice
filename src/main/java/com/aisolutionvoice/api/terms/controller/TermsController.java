package com.aisolutionvoice.api.terms.controller;

import com.aisolutionvoice.api.auth.dto.LoginRequestDto;
import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
import com.aisolutionvoice.api.terms.domain.TermsType;
import com.aisolutionvoice.api.terms.dto.TermsDto;
import com.aisolutionvoice.api.terms.dto.TermsTypeDto;
import com.aisolutionvoice.api.terms.service.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
@Tag(name = "Term", description = "약관 관리 API")
@Slf4j
public class TermsController {
    private final TermsService termsService;
    @GetMapping("/types")
    public List<TermsTypeDto> getTermsTypes() {
        return Arrays.stream(TermsType.values())
                .map(TermsTypeDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/applied")
    public List<TermsDto> getAppliedTerms() {
        return termsService.getAllAppliedTerms().stream()
                .map(TermsDto::from)
                .collect(Collectors.toList());
    }
}

package com.aisolutionvoice.api.auth.service;

import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.terms.dto.AgreedTermDto;
import com.aisolutionvoice.api.terms.entity.Terms;
import com.aisolutionvoice.api.terms.repository.TermsRepository;
import com.aisolutionvoice.api.terms.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TermsService termsService;
    private final MemberService memberService;
    @Transactional
    public void signUp(SignupRequestDto request) {
        // 1. 필수 약관 확인
        termsService.validateRequiredTermsAgreed(request.getAgreedTerms());

        // 2. 회원 저장
        Member member = memberService.createMember(request);

        // 3. 약관저장
        termsService.assignTermsToMember(member, request.getAgreedTerms());
    }

}

package com.aisolutionvoice.api.auth.service;

import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
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
    @Transactional
    public void signUp(SignupRequestDto request) {
        // 1. 필수 약관 목록 조회
        termsService.validateRequiredTermsAgreed(request.getAgreedTerms());

        // 3. 사용자 저장 (생략 가능)
//        User user = new User();
//        user.setNickname(request.getNickname());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        userRepository.save(user);
//
//        // 4. 동의 내역 저장
//        for (AgreedTermDto agreed : request.getAgreedTerms()) {
//            userTermsRepository.save(new UserTerms(user, agreed.getTermId(), agreed.getAgreed()));
//        }
    }

}

package com.aisolutionvoice.api.terms.service;

import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.terms.dto.AgreedTermDto;
import com.aisolutionvoice.api.terms.entity.AppliedTerms;
import com.aisolutionvoice.api.terms.entity.MemberTerms;
import com.aisolutionvoice.api.terms.entity.Terms;
import com.aisolutionvoice.api.terms.repository.AppliedTermsRepository;
import com.aisolutionvoice.api.terms.repository.MemberTermsRepository;
import com.aisolutionvoice.api.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final AppliedTermsRepository appliedTermsRepository;
    private final TermsRepository termsRepository;
    private final MemberTermsRepository memberTermsRepository;

    @Transactional(readOnly = true)
    public List<Terms> getAllAppliedTerms() {
        return appliedTermsRepository.findAll().stream()
                .map(AppliedTerms::getTerms)
                .collect(Collectors.toList());
    }

    public void validateRequiredTermsAgreed(List<AgreedTermDto> agreedTerms) {
        List<Terms> requiredTerms = termsRepository.findByIsRequiredTrue();
        Set<Long> agreedIds = agreedTerms.stream()
                .filter(AgreedTermDto::getAgreed)
                .map(AgreedTermDto::getTermId)
                .collect(Collectors.toSet());

        for (Terms term : requiredTerms) {
            if (!agreedIds.contains(term.getTermId())) {
                throw new IllegalArgumentException("필수 약관 누락: " + term.getTitle());
            }
        }
    }
    public void assignTermsToMember(Member member, List<AgreedTermDto> agreedTerms) {
        for (AgreedTermDto dto : agreedTerms) {
            if (!dto.getAgreed()) continue;

            Terms terms = termsRepository.findById(dto.getTermId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 약관"));

            MemberTerms memberTerms = MemberTerms.builder()
                    .member(member)
                    .terms(terms)
                    .agreedAt(LocalDateTime.now())
                    .build();

            memberTermsRepository.save(memberTerms);
        }
    }
}
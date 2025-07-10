package com.aisolutionvoice.api.terms.service;

import com.aisolutionvoice.api.terms.dto.AgreedTermDto;
import com.aisolutionvoice.api.terms.entity.AppliedTerms;
import com.aisolutionvoice.api.terms.entity.Terms;
import com.aisolutionvoice.api.terms.repository.AppliedTermsRepository;
import com.aisolutionvoice.api.terms.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final AppliedTermsRepository appliedTermsRepository;
    private final TermsRepository termsRepository;

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
            if (!agreedIds.contains(term.getId())) {
                throw new IllegalArgumentException("필수 약관 누락: " + term.getTitle());
            }
        }
    }
}
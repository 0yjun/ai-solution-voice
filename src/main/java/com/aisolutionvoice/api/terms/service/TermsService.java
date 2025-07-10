package com.aisolutionvoice.api.terms.service;

import com.aisolutionvoice.api.terms.entity.AppliedTerms;
import com.aisolutionvoice.api.terms.entity.Terms;
import com.aisolutionvoice.api.terms.repository.AppliedTermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final AppliedTermsRepository appliedTermsRepository;

    @Transactional(readOnly = true)
    public List<Terms> getAllAppliedTerms() {
        return appliedTermsRepository.findAll().stream()
                .map(AppliedTerms::getTerms)
                .collect(Collectors.toList());
    }
}
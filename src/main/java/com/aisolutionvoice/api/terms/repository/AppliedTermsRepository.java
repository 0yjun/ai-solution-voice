package com.aisolutionvoice.api.terms.repository;

import com.aisolutionvoice.api.terms.domain.TermsType;
import com.aisolutionvoice.api.terms.entity.AppliedTerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppliedTermsRepository extends JpaRepository<AppliedTerms, Long> {

    // 전체 적용된 약관 (타입별 1개씩)
    List<AppliedTerms> findAll();

    // 정렬 포함 (선택)
    List<AppliedTerms> findAllByOrderByTypeAsc();

    // 특정 타입만 조회
    Optional<AppliedTerms> findByType(TermsType type);
}

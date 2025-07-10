package com.aisolutionvoice.api.terms.repository;

import com.aisolutionvoice.api.terms.domain.TermsType;
import com.aisolutionvoice.api.terms.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {
    // 특정 약관 타입의 최신 버전 조회 (버전이 unique한 경우)
    Optional<Terms> findTopByTypeOrderByCreatedAtDesc(TermsType type);

    // 특정 타입의 약관 전체 조회
    List<Terms> findByType(TermsType type);

    // 모든 최신 약관(예: 동의 화면에서 사용)
    List<Terms> findAllByOrderByIsRequiredDescCreatedAtDesc();
}
package com.aisolutionvoice.api.terms.repository;

import com.aisolutionvoice.api.terms.entity.MemberTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTermsRepository extends JpaRepository<MemberTerms, Long> {
}

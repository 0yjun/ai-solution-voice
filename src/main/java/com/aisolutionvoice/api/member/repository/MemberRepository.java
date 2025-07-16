package com.aisolutionvoice.api.member.repository;

import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.menu.entity.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);
}

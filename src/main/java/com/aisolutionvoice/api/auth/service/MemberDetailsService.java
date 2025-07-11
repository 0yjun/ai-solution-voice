package com.aisolutionvoice.api.auth.service;

import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId));

        System.out.println(member.getPassword());
        System.out.println(member.getLoginId());
        System.out.println(member.getUserId());
        return User.withUsername(member.getLoginId())
                .password(member.getPassword())
                .authorities("ROLE_" +member.getRole().name())
                .build();
    }
}

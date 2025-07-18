package com.aisolutionvoice.api.member.service;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.auth.dto.SignupRequestDto;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.repository.MemberRepository;
import com.aisolutionvoice.exception.CustomException;
import com.aisolutionvoice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMemberByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId).orElse(null);
    }

    public List<Member> getAllMembers(){
        return memberRepository.findAll();
    }

    @Transactional
    public Member createMember(SignupRequestDto dto) {
        if(memberRepository.existsByLoginId(dto.getLoginId())){
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(encodedPassword)
                .di(null)
                .birth(null)
                .role(Role.USER) // 기본 역할 부여
                .build();

        return memberRepository.save(member);
    }
}

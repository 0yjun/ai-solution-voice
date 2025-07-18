package com.aisolutionvoice.api.member.controller;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.member.entity.Member;
import com.aisolutionvoice.api.member.service.MemberService;
import com.aisolutionvoice.api.menu.dto.MenuClientDto;
import com.aisolutionvoice.api.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Member> memberList = memberService.getAllMembers();
        return ResponseEntity.ok(memberList);
    }
}

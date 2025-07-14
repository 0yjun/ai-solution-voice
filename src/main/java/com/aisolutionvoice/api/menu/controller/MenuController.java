package com.aisolutionvoice.api.menu.controller;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.menu.dto.MenuClientDto;
import com.aisolutionvoice.api.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    private final MenuService menuService;
    @GetMapping
    public ResponseEntity<Object> getClientMenu(Authentication authentication) {
        // 1) authentication 으로 부터 권한 추출
        String roleString = authentication
                .getAuthorities().iterator().next().getAuthority()
                .replaceFirst("ROLE_","");
        System.out.println(roleString);
        System.out.println(Role.values());
        Role userRole = Role.valueOf(roleString);

        // 3) 서비스 호출 (단일 Role 전달)
        List<MenuClientDto> menus = menuService.getAccessibleMenusByUserRole(userRole);

        return ResponseEntity.ok(menus);
    }
}

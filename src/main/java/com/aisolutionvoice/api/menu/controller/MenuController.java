package com.aisolutionvoice.api.menu.controller;

import com.aisolutionvoice.api.menu.dto.MenuClientDto;
import com.aisolutionvoice.api.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;
    @GetMapping
    public ResponseEntity<Object> getMenu(Authentication authentication) {
//        // 1) authentication 으로 부터 권한 추출
//        Role userRole = jwtTokenProvider.getRoleFromAuthentication(authentication);
//
//        // 3) 서비스 호출 (단일 Role 전달)
//        List<MenuClientDto> menus = menuService.getAccessibleMenusByUserRole(userRole);
//
//        return ResponseEntity.ok(result);
        return null;
    }
}

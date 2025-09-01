package com.aisolutionvoice.api.auth.dto;

import com.aisolutionvoice.api.Role.domain.Role;
import com.aisolutionvoice.api.terms.dto.AgreedTermDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "회원가입 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

        @Schema(description = "로그인 ID", example = "admin1")
        @NotBlank
        private String loginId;

        @Schema(description = "계정", example = "account1")
        @NotBlank
        private String account;

        @Schema(description = "비밀번호", example = "securePass123!")
        @NotBlank
        private String password;

        @Schema(description = "권한", example = "ROLE_SYSTEM_ADMIN")
        @NotNull
        private Role role;

        @Schema(description = "약관 동의 여부 리스트", example = "개인정보 보호 여부 확인, 마케팅 동의 여부 확인")
        @NotNull
        private List<AgreedTermDto> agreedTerms;
}
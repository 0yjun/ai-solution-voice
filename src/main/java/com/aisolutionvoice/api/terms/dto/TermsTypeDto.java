package com.aisolutionvoice.api.terms.dto;

import com.aisolutionvoice.api.terms.domain.TermsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TermsTypeDto {
    private String value;
    private String label;

    public static TermsTypeDto from(TermsType type) {
        return new TermsTypeDto(type.name(), type.getLabel());
    }
}

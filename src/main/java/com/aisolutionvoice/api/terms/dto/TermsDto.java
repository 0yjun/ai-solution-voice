package com.aisolutionvoice.api.terms.dto;

import com.aisolutionvoice.api.terms.domain.TermsType;
import com.aisolutionvoice.api.terms.entity.Terms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TermsDto {
    private Long id;
    private TermsType type;
    private String title;
    private String content;
    private String version;
    private boolean isRequired;

    public TermsDto(Terms terms) {
        this.id = terms.getTermId();
        this.type = terms.getType();
        this.title = terms.getTitle();
        this.content = terms.getContent();
        this.version = terms.getVersion();
        this.isRequired = terms.isRequired();
    }

    public static TermsDto from(Terms terms) {
        return new TermsDto(terms);
    }
}

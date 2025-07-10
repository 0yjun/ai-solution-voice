package com.aisolutionvoice.api.terms.domain;

public enum TermsType {
    PRIVACY("개인정보 처리방침"),
    TERMS("이용약관");

    private final String label;

    TermsType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

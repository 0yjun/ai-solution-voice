package com.aisolutionvoice.api.terms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgreedTermDto {
    @NotNull
    private Long termId;

    @NotNull
    private Boolean agreed;
}

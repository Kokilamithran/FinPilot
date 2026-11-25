package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank
    private String categoryName;

    @NotBlank
    private String categoryType;

    private String icon;

    private String colour;

    private Long parentCategoryId;

    private Boolean isSystem;

    private Long userId;

    private Integer displayOrder;
}
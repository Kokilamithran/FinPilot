package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ImportHistoryRequest {

    private Long userId;

    @NotNull
    private Long accountId;

    @NotBlank
    private String fileName;

    @NotBlank
    private String fileFormat;

    private Integer totalRecords;

    private Integer successCount;

    private Integer duplicateCount;

    private String status;

    private String remarks;
}
package com.finpilot.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImportHistoryResponse {

    private Long importId;

    private Long userId;

    private Long accountId;

    private String fileName;

    private String fileFormat;

    private LocalDateTime importedAt;

    private Integer totalRecords;

    private Integer successCount;

    private Integer duplicateCount;

    private String status;

    private String remarks;
}
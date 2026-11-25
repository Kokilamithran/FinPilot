package com.finpilot.mapper;

import com.finpilot.dto.request.ImportHistoryRequest;
import com.finpilot.dto.response.ImportHistoryResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.ImportHistory;
import com.finpilot.entity.User;

public class ImportHistoryMapper {

    public static ImportHistory toEntity(ImportHistoryRequest request,
                                         User user,
                                         Account account) {

        ImportHistory history = new ImportHistory();

        history.setUser(user);
        history.setAccount(account);
        history.setFileName(request.getFileName());
        history.setFileFormat(request.getFileFormat());
        history.setTotalRecords(request.getTotalRecords());
        history.setSuccessCount(request.getSuccessCount());
        history.setDuplicateCount(request.getDuplicateCount());
        history.setStatus(request.getStatus());
        history.setRemarks(request.getRemarks());

        return history;
    }

    public static ImportHistoryResponse toResponse(ImportHistory history) {

        ImportHistoryResponse response = new ImportHistoryResponse();

        response.setImportId(history.getImportId());
        response.setUserId(history.getUser().getUserId());
        response.setAccountId(history.getAccount().getAccountId());
        response.setFileName(history.getFileName());
        response.setFileFormat(history.getFileFormat());
        response.setImportedAt(history.getImportedAt());
        response.setTotalRecords(history.getTotalRecords());
        response.setSuccessCount(history.getSuccessCount());
        response.setDuplicateCount(history.getDuplicateCount());
        response.setStatus(history.getStatus());
        response.setRemarks(history.getRemarks());

        return response;
    }
}
package org.example.dto;
import java.util.List;

public class CsvUploadResponseDto {
    private int totalRows;
    private int successfulRows;
    private int failedRows;
    private List<String> errors;
    private String message;

    public CsvUploadResponseDto() {}

    public CsvUploadResponseDto(int totalRows, int successfulRows, int failedRows, List<String> errors, String message) {
        this.totalRows = totalRows;
        this.successfulRows = successfulRows;
        this.failedRows = failedRows;
        this.errors = errors;
        this.message = message;
    }

    // Getters and Setters
    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }

    public int getSuccessfulRows() { return successfulRows; }
    public void setSuccessfulRows(int successfulRows) { this.successfulRows = successfulRows; }

    public int getFailedRows() { return failedRows; }
    public void setFailedRows(int failedRows) { this.failedRows = failedRows; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
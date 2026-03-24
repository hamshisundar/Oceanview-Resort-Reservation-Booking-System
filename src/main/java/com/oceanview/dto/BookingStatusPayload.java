package com.oceanview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingStatusPayload {
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

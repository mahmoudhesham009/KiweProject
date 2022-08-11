package com.mahmoudh.kiwe.dto;

public class InsertProductsResponse {
    Integer uploaded;
    Integer ignored;

    public InsertProductsResponse(Integer uploaded, Integer ignored) {
        this.uploaded = uploaded;
        this.ignored = ignored;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }

    public Integer getIgnored() {
        return ignored;
    }

    public void setIgnored(Integer ignored) {
        this.ignored = ignored;
    }
}

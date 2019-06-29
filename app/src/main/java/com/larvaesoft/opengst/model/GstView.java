package com.larvaesoft.opengst.model;

public class GstView {
    private Double base;
    private String cgstLabel;
    private String sgstLabel;
    private String gstLabel;
    private Double cgstValue;
    private Double sgstValue;
    private Double gstValue;
    private Double total;

    public void setCgstLabel(String cgstLabel) {
        this.cgstLabel = "CGST(" + cgstLabel + "%)";
    }

    public String getSgstLabel() {
        return sgstLabel;
    }

    public void setSgstLabel(String sgstLabel) {
        this.sgstLabel = "SGST(" + sgstLabel + "%)";
    }

    public String getGstLabel() {
        return gstLabel;
    }

    public void setGstLabel(String gstLabel) {
        this.gstLabel = "GST(" + gstLabel + "%)";
    }

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public String getCgstLabel() {
        return cgstLabel;
    }

    public Double getCgstValue() {
        return cgstValue;
    }

    public void setCgstValue(Double cgstValue) {
        this.cgstValue = cgstValue;
    }

    public Double getSgstValue() {
        return sgstValue;
    }

    public void setSgstValue(Double sgstValue) {
        this.sgstValue = sgstValue;
    }

    public Double getGstValue() {
        return gstValue;
    }

    public void setGstValue(Double gstValue) {
        this.gstValue = gstValue;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

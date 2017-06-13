package cn.zhou.mapper.model;

import java.math.BigDecimal;

public class EciLocation {
    private Long eci;

    private String eciName;

    private String cellName;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Integer coordX;

    private Integer coordY;

    private String azimuth;

    private String lacName;

    private String coverarea;

    private String street;

    private String lac;

    private String ci;

    public Long getEci() {
        return eci;
    }

    public void setEci(Long eci) {
        this.eci = eci;
    }

    public String getEciName() {
        return eciName;
    }

    public void setEciName(String eciName) {
        this.eciName = eciName == null ? null : eciName.trim();
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName == null ? null : cellName.trim();
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Integer getCoordX() {
        return coordX;
    }

    public void setCoordX(Integer coordX) {
        this.coordX = coordX;
    }

    public Integer getCoordY() {
        return coordY;
    }

    public void setCoordY(Integer coordY) {
        this.coordY = coordY;
    }

    public String getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth == null ? null : azimuth.trim();
    }

    public String getLacName() {
        return lacName;
    }

    public void setLacName(String lacName) {
        this.lacName = lacName == null ? null : lacName.trim();
    }

    public String getCoverarea() {
        return coverarea;
    }

    public void setCoverarea(String coverarea) {
        this.coverarea = coverarea == null ? null : coverarea.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac == null ? null : lac.trim();
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci == null ? null : ci.trim();
    }
}
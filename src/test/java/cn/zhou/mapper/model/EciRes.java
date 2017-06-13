package cn.zhou.mapper.model;

public class EciRes {
    private Integer id;

    private Integer sceneId;

    private Long eci;

    private Integer amount;

    private String time;

    private String grain;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public Long getEci() {
        return eci;
    }

    public void setEci(Long eci) {
        this.eci = eci;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getGrain() {
        return grain;
    }

    public void setGrain(String grain) {
        this.grain = grain == null ? null : grain.trim();
    }
}
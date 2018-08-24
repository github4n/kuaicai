package com.caipiao.entity;

/**
 * 遗漏次数峰值统计
 */
public class OmissionPeak {
    private Integer id;

    private String omissioncode;

    private String startexpect;

    private String openExpect;

    private Integer startomissionnum;

    private Integer currentomissionnum;

    private String updatetime;

    private Integer state;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenExpect() {
        return openExpect;
    }

    public void setOpenExpect(String openExpect) {
        this.openExpect = openExpect;
    }

    public String getOmissioncode() {
        return omissioncode;
    }

    public void setOmissioncode(String omissioncode) {
        this.omissioncode = omissioncode == null ? null : omissioncode.trim();
    }

    public String getStartexpect() {
        return startexpect;
    }

    public void setStartexpect(String startexpect) {
        this.startexpect = startexpect == null ? null : startexpect.trim();
    }

    public Integer getStartomissionnum() {
        return startomissionnum;
    }

    public void setStartomissionnum(Integer startomissionnum) {
        this.startomissionnum = startomissionnum;
    }

    public Integer getCurrentomissionnum() {
        return currentomissionnum;
    }

    public void setCurrentomissionnum(Integer currentomissionnum) {
        this.currentomissionnum = currentomissionnum;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
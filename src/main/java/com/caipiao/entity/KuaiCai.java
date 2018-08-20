package com.caipiao.entity;

public class KuaiCai {
    private Integer id;

    private String expect;

    private String opencodesort;

    private String opencode;

    private String opentime;

    private String opentimestamp;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect == null ? null : expect.trim();
    }

    public String getOpencodesort() {
        return opencodesort;
    }

    public void setOpencodesort(String opencodesort) {
        this.opencodesort = opencodesort == null ? null : opencodesort.trim();
    }

    public String getOpencode() {
        return opencode;
    }

    public void setOpencode(String opencode) {
        this.opencode = opencode == null ? null : opencode.trim();
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime == null ? null : opentime.trim();
    }

    public String getOpentimestamp() {
        return opentimestamp;
    }

    public void setOpentimestamp(String opentimestamp) {
        this.opentimestamp = opentimestamp == null ? null : opentimestamp.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
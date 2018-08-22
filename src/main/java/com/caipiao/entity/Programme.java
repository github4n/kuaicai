package com.caipiao.entity;

import java.util.List;

public class Programme {
    private Integer id;

    /** 开始追号的前一期号*/
    private String expect;

    /** 方案状态，0:进行中，1:完结并盈利，2:结束但亏损(止损) */
    private Integer state;

    /** 追号,不支持复式 */
    private String chasecode;

    /** 任3,4,5,6,7,8,9 */
    private Integer type;

    /** 遗漏多少期后开始追号，如100期后开始追 则值为100 */
    private Integer chasestart;

    /** 止损，最多追多少期 */
    private Integer stoploss;

    /** 盈利，负数为亏损 */
    private Integer profit;

    /** 计划表 */
    private List<Plan> planList;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getChasecode() {
        return chasecode;
    }

    public void setChasecode(String chasecode) {
        this.chasecode = chasecode == null ? null : chasecode.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getChasestart() {
        return chasestart;
    }

    public void setChasestart(Integer chasestart) {
        this.chasestart = chasestart;
    }

    public Integer getStoploss() {
        return stoploss;
    }

    public void setStoploss(Integer stoploss) {
        this.stoploss = stoploss;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }
}
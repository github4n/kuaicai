package com.caipiao.entity;

public class Plan {
    private Integer id;

    private Integer programmeid;

    private Integer amount;

    private Integer totalamount;

    private Integer openamount;

    private Integer profit;

    private Integer parent;

    private Integer planno;

    public Plan() {
    }

    public static Plan getPre(Integer programmeid){
        Plan plan = new Plan();
        plan.setProgrammeid(programmeid);
        plan.setPlanno(0);
        plan.setParent(0);
        plan.setTotalamount(0);
        plan.setProfit(0);
        plan.setOpenamount(0);
        plan.setAmount(0);
        return plan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProgrammeid() {
        return programmeid;
    }

    public void setProgrammeid(Integer programmeid) {
        this.programmeid = programmeid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(Integer totalamount) {
        this.totalamount = totalamount;
    }

    public Integer getOpenamount() {
        return openamount;
    }

    public void setOpenamount(Integer openamount) {
        this.openamount = openamount;
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getPlanno() {
        return planno;
    }

    public void setPlanno(Integer planno) {
        this.planno = planno;
    }
}
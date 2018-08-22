package com.caipiao.entity;

public class Plan {
    private Integer id;

    private Integer programmeid;

    /** 本期投入金额 */
    private Integer amount;

    /** 总投入成本,加上本期投入amount */
    private Integer totalamount;

    /** 当期如开出，则中奖XX元 */
    private Integer openamount;

    /** 盈利，负数为亏损 */
    private Integer profit;

    /** 计划第一步parent值为0, 子计划的值为第一步计划的id值 */
    private Integer parent;

    /** 第几步 */
    private Integer planno;

    /** 该步追的期号 */
    private String expect;

    /** expect对应的开出 */
    private String opencode;

    /** 计划状态，0未进行， 1已完成*/
    private Integer state;

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
        plan.setState(0);
        return plan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getOpencode() {
        return opencode;
    }

    public void setOpencode(String opencode) {
        this.opencode = opencode;
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
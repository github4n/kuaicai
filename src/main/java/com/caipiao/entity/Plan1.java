package com.caipiao.entity;

/**
 * 计划
 *
 * @author zhangjj
 * @create 2018-08-21 11:44
 **/
public class Plan1 {

    private Integer id;

    private Integer programmeID;

    /** 计划第一步parent值为0, 子计划的值为第一步计划的id值 */
    private Integer parent;

    /** 本期投入金额 */
    private Integer amount;

    /** 总投入成本,加上本期投入amount */
    private Integer totalAmount;

    /** 当期如开出，则中奖XX元 */
    private Integer openAmount;

    /** 盈利，负数为亏损 */
    private Integer profit;

}

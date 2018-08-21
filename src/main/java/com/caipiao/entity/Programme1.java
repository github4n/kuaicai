package com.caipiao.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 方案
 *
 * @author zhangjj
 * @create 2018-08-21 11:17
 **/
@Getter
@Setter
public class Programme1 {

    private Integer id;

    /** 开始追号的前一期号*/
    private String expect;

    /** 方案状态，0:进行中，1:完结并盈利，2:结束但亏损(止损) */
    private Integer state;

    /** 追号,不支持复式 */
    private String chaseCode;

    /** 任3,4,5,6,7,8,9 */
    private Integer type;

    /** 遗漏多少期后开始追号，如100期后开始追 则值为100 */
    private Integer chaseStart;

    /** 止损，最多追多少期 */
    private Integer stopLoss;

    /** 盈利，负数为亏损 */
    private Integer profit;

    /** 计划表 */
    private List<Plan1> planList;

}

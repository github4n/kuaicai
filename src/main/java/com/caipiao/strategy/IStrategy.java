package com.caipiao.strategy;

import com.caipiao.entity.Plan;

/**
 * 生成计划策略
 *
 * @author zhangjj
 * @create 2018-08-21 12:00
 **/
public interface IStrategy {

    /**
     * 根据上一步结果生成下一步计划
     * @param
     * @author zhangjj
     * @Date 2018/8/21 13:40
     * @return
     * @exception
     *
     */
    Plan nextStep(Plan lastStep);

}

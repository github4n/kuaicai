package com.caipiao.strategy;

import com.caipiao.contants.Constants;
import com.caipiao.entity.Plan;
import com.caipiao.entity.Programme;
import com.caipiao.mapper.ProgrammeMapper;

import javax.annotation.Resource;

/**
 * 模板
 *
 * @author zhangjj
 * @create 2018-08-22 14:14
 **/
public abstract class AbstractStrategy implements IStrategy {

    @Resource
    private ProgrammeMapper programmeMapper;

    @Override
    public Plan nextStep(Plan lastStep) {
        Programme programme = programmeMapper.selectByPrimaryKey(lastStep.getProgrammeid());
        Integer nextAmount = getNextAmount(lastStep.getTotalamount(), programme.getType());

        Plan nextPlan = new Plan();
        nextPlan.setAmount(nextAmount);
        int openamount = nextAmount/2 * Constants.OPEN_MONEY_MAP.get(programme.getType());
        nextPlan.setOpenamount(openamount);
        if(lastStep.getPlanno() == 0){
            nextPlan.setParent(0);
        }else{
            nextPlan.setParent(lastStep.getParent());
        }
        nextPlan.setProfit(openamount - nextAmount - lastStep.getTotalamount());
        nextPlan.setProgrammeid(lastStep.getProgrammeid());
        nextPlan.setTotalamount(nextAmount + lastStep.getTotalamount());
        nextPlan.setPlanno(lastStep.getPlanno() + 1);
        nextPlan.setState(0);

        return nextPlan;
    }

    public abstract Integer getNextAmount(Integer totalAmount, Integer type);
}

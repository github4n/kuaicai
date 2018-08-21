package com.caipiao.strategy;

import com.caipiao.contants.Constants;
import com.caipiao.entity.Plan;
import com.caipiao.entity.Programme;
import com.caipiao.mapper.ProgrammeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 保守操作
 *
 * @author zhangjj
 * @create 2018-08-21 14:10
 **/
@Service("conservativeStrategy")
public class ConservativeStrategy implements IStrategy {

    @Resource
    private ProgrammeMapper programmeMapper;

    @Override
    public Plan nextStep(Plan lastStep) {
        Programme programme = programmeMapper.selectByPrimaryKey(lastStep.getProgrammeid());
        Integer nextAmount = getConservativeValue(lastStep.getTotalamount(), programme.getType());

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

        return nextPlan;
    }

    private Integer getConservativeValue(Integer totalAmount, Integer type){
//        totalAmount.floatValue() + x < Constants.OPEN_MONEY_MAP.get(type) * x;
//        totalAmount.floatValue() < Constants.OPEN_MONEY_MAP.get(type) * x  - x;
//        totalAmount.floatValue()/(Constants.OPEN_MONEY_MAP.get(type) - 1) < x
//        x > totalAmount.floatValue()/(Constants.OPEN_MONEY_MAP.get(type) - 1)
        Float x = totalAmount.floatValue()/(Constants.OPEN_MONEY_MAP.get(type) - 1);

        if((x.intValue() + 1) % 2 == 0){
            return x.intValue() + 1;
        }else{
            return x.intValue() + 2;
        }
    }

    public static void main(String[] args) {
        Float x = 3.8F;

        System.out.println(x.intValue());
    }

}

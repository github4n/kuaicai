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
public class ConservativeStrategy extends AbstractStrategy {

    @Override
    public Integer getNextAmount(Integer totalAmount, Integer type){
//        totalAmount.floatValue() + x < (Constants.OPEN_MONEY_MAP.get(type) / 2) * x;
//        totalAmount.floatValue() * 2 + 2x < Constants.OPEN_MONEY_MAP.get(type) * x;
//        totalAmount.floatValue() * 2 < Constants.OPEN_MONEY_MAP.get(type) * x  - 2x;
//        totalAmount.floatValue()*2/(Constants.OPEN_MONEY_MAP.get(type) - 2) < x
//        x > totalAmount.floatValue()*2/(Constants.OPEN_MONEY_MAP.get(type) - 2)
        Float x = (totalAmount.floatValue() * 2)/(Constants.OPEN_MONEY_MAP.get(type) - 2);

        if((x.intValue() + 1) % 2 == 0){
            return x.intValue() + 1;
        }else{
            return x.intValue() + 2;
        }
    }

    public static void main(String[] args) {
        System.out.println(new ConservativeStrategy().getNextAmount(20, 3));
    }

}

package com.caipiao.strategy;

import com.caipiao.contants.Constants;
import org.springframework.stereotype.Service;

/**
 * 激进
 *
 * @author zhangjj
 * @create 2018-08-22 14:16
 **/
@Service("radicalStrategy")
public class RadicalStrategy extends AbstractStrategy {

    private final static Integer COEFFICIENT = 2;//增量系数

    @Override
    public Integer getNextAmount(Integer totalAmount, Integer type) {
//        totalAmount.floatValue() + x < (Constants.OPEN_MONEY_MAP.get(type) / 2) * x;
//        totalAmount.floatValue() * 2 + 2x < Constants.OPEN_MONEY_MAP.get(type) * x;
//        totalAmount.floatValue() * 2 < Constants.OPEN_MONEY_MAP.get(type) * x  - 2x;
//        totalAmount.floatValue()*2/(Constants.OPEN_MONEY_MAP.get(type) - 2) < x
//        x > totalAmount.floatValue()*2/(Constants.OPEN_MONEY_MAP.get(type) - 2)
        Float x = (totalAmount.floatValue() * 2)/(Constants.OPEN_MONEY_MAP.get(type) - 2);

        if((x.intValue() + 1) % 2 == 0){
            return x.intValue() + 1 + COEFFICIENT;
        }else{
            return x.intValue() + 2 + COEFFICIENT;
        }
    }
}

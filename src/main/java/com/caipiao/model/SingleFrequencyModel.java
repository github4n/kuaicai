package com.caipiao.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 单个数字出现的频率
 *
 * @author zhangjj
 * @create 2018-08-20 15:04
 **/
@Getter
@Setter
public class SingleFrequencyModel implements Comparable<SingleFrequencyModel>{

    /** 数值 */
    private String number;

    /** 出现的次数*/
    private Integer count;

    /** 百分比*/
    private String percentage;

    @Override
    public int compareTo(SingleFrequencyModel o) {
        return count.compareTo(o.getCount());
    }
}

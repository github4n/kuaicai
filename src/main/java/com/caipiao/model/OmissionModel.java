package com.caipiao.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangjj
 * @create 2018-08-13 17:23
 **/
@Getter
@Setter
public class OmissionModel implements Comparable<OmissionModel>{

    /** 组合 */
    private String combination;

    /** 遗漏基数*/
    private Integer omissionNum;

    @Override
    public int compareTo(OmissionModel o) {
        return o.getOmissionNum().compareTo(omissionNum);
    }
}

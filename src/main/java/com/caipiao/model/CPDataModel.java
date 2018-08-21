package com.caipiao.model;

import com.caipiao.entity.KuaiCai;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangjj
 * @create 2018-08-13 13:47
 **/
@Getter
@Setter
public class CPDataModel {

    private Integer rows;

    private String code;

    private String info;

    private List<KuaiCai> data;
}

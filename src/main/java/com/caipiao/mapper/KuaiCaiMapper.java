package com.caipiao.mapper;

import com.caipiao.entity.KuaiCai;

public interface KuaiCaiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(KuaiCai record);

    int insertSelective(KuaiCai record);

    KuaiCai selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KuaiCai record);

    int updateByPrimaryKey(KuaiCai record);
}
package com.caipiao.mapper;

import com.caipiao.entity.KuaiCai;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KuaiCaiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(KuaiCai record);

    int insertSelective(KuaiCai record);

    KuaiCai selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KuaiCai record);

    int updateByPrimaryKey(KuaiCai record);

    KuaiCai selectByExpect(String expect);

    List<KuaiCai> selectAll(@Param("todayStr") String todayStr);
}
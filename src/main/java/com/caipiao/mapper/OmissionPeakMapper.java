package com.caipiao.mapper;

import com.caipiao.entity.OmissionPeak;
import org.apache.ibatis.annotations.Param;

public interface OmissionPeakMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OmissionPeak record);

    int insertSelective(OmissionPeak record);

    OmissionPeak selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OmissionPeak record);

    int updateByPrimaryKey(OmissionPeak record);

    OmissionPeak find(@Param("combination") String combination, @Param("state") int state);
}
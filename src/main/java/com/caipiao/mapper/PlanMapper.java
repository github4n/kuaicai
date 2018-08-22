package com.caipiao.mapper;

import com.caipiao.entity.Plan;

import java.util.List;

public interface PlanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Plan record);

    int insertSelective(Plan record);

    Plan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Plan record);

    int updateByPrimaryKey(Plan record);

    List<Plan> findByProgramme(Integer programmeID);

    Plan findNextPlan(Integer programmeID);
}
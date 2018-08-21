package com.caipiao.mapper;

import com.caipiao.entity.Programme;

import java.util.List;

public interface ProgrammeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Programme record);

    int insertSelective(Programme record);

    Programme selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Programme record);

    int updateByPrimaryKey(Programme record);

    /**
     * 进行中的策略
     * @param
     * @author zhangjj
     * @Date 2018/8/21 16:32
     * @return
     * @exception
     *
     */
    Programme find(String combination);

    List<Programme> findAll();
}
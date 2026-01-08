package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.pojo.DefectReception;

import java.util.Map;

public interface DefectReceptionService {

   Integer addDefectReception(Map<String,Object> params);

    PageInfo<DefectReception> searchDefectReceptions(DefectTaskCondition condition);

    Integer updateDefectReception(Map<String, Object> params);

    Integer deleteDefectReception(long id);
}

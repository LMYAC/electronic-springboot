package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.pojo.InspectionReception;

import java.util.Map;

public interface InspectionReceptionService {

    Integer addInspectionReception(Map<String,Object> params);

    PageInfo<InspectionReception> searchInspectionReception(InspectionTaskCondition condition);

    Integer updateInspectionReception(Map<String, Object> params);

    Integer deleteInspectionReception(Long id);
}

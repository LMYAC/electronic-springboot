package com.qf.electronic.mapper;

import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.pojo.InspectionReception;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InspectionReceptionMapper {

    Integer addInspectionReception(@Param("params") Map<String,Object> params);

    List<InspectionReception> searchInspectionReception(@Param("condition") InspectionTaskCondition condition);

    int updateInspectionReception(@Param("params") Map<String, Object> params);

    long getInspectionTaskId(@Param("id")int id);

    Integer deleteInspectionReception(@Param("id") Long id);
}

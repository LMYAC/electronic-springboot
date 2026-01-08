package com.qf.electronic.mapper;

import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.pojo.DefectReception;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface DefectReceptionMapper {

    Integer addDefectReception(@Param("params") Map<String, Object> params);

    List<DefectReception> searchDefectReceptions(@Param("condition") DefectTaskCondition condition);

    Integer updateDefectReception(@Param("params") Map<String, Object> params);

    Integer deleteDefectReception(@Param("id") long id);

    long getTaskId(@Param("id") int id);
}

package com.qf.electronic.mapper;

import com.qf.electronic.pojo.DefectData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DefectDataMapper {

    List<DefectData> getDefectData(@Param("tableName") String tableName);

    Integer addDefectData(@Param("tableName")String tableName, @Param("params") Map<String, Object> params);

    Integer updateDefectData(@Param("tableName")String tableName, @Param("params") Map<String, Object> params);

    Integer deleteDefectData(@Param("tableName")String tableName, @Param("id")int id);
}

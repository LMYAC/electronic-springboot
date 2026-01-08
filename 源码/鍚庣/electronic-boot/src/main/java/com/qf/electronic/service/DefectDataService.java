package com.qf.electronic.service;

import com.qf.electronic.pojo.DefectData;

import java.util.List;
import java.util.Map;

public interface DefectDataService {

    List<DefectData> getDefectData(String dataType);

    Integer addDefectData(String dataType, Map<String, Object> params);

    Integer updateDefectData(String dataType, Map<String, Object> params);

    Integer deleteDefectData(String dataType, int id);
}

package com.qf.electronic.service.impl;

import com.qf.electronic.mapper.DefectDataMapper;
import com.qf.electronic.pojo.DefectData;
import com.qf.electronic.service.DefectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DefectDataServiceImpl implements DefectDataService {

    @Autowired
    private DefectDataMapper defectDataMapper;

    @Transactional(readOnly = true)
    @Override
    public List<DefectData> getDefectData(String dataType) {
        String tableName = getTableName(dataType);
        return defectDataMapper.getDefectData(tableName);
    }

    private String getTableName(String dataType) {
        if("defectType".equals(dataType)){
            return "defect_type";
        } else if("defectLevel".equals(dataType)){
            return "defect_level";
        } else throw new RuntimeException(dataType + "不正确");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addDefectData(String dataType, Map<String, Object> params) {
        String tableName = getTableName(dataType);
        return defectDataMapper.addDefectData(tableName, params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateDefectData(String dataType, Map<String, Object> params) {
        String tableName = getTableName(dataType);
        return defectDataMapper.updateDefectData(tableName, params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteDefectData(String dataType, int id) {
        String tableName = getTableName(dataType);
        return defectDataMapper.deleteDefectData(tableName, id);
    }
}

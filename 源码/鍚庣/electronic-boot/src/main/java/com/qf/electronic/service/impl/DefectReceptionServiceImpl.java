package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.mapper.DefectReceptionMapper;
import com.qf.electronic.pojo.DefectReception;
import com.qf.electronic.service.DefectReceptionService;
import com.qf.electronic.service.DefectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefectReceptionServiceImpl implements DefectReceptionService {

    @Autowired
    private DefectReceptionMapper defectReceptionMapper;

    @Autowired
    private DefectTaskService defectTaskService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addDefectReception(Map<String, Object> params) {
        return defectReceptionMapper.addDefectReception(params);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<DefectReception> searchDefectReceptions(DefectTaskCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<DefectReception> receptions = defectReceptionMapper.searchDefectReceptions(condition);
        return new PageInfo<>(receptions);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateDefectReception(Map<String, Object> params) {
        Integer result = defectReceptionMapper.updateDefectReception(params);
        if(result == 1){
            if(params.containsKey("state")){
                int state = (int) params.get("state");
                if(state == 2){//考虑审核通过：需要翻转消缺任务的执行状态为完成
                    long taskId = defectReceptionMapper.getTaskId((int) params.get("id"));
                    Map<String,Object> taskParams = new HashMap<>();
                    taskParams.put("id", taskId);
                    taskParams.put("state", 3);
                    defectTaskService.updateDefectTask(taskParams);
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteDefectReception(long id) {
        return defectReceptionMapper.deleteDefectReception(id);
    }
}

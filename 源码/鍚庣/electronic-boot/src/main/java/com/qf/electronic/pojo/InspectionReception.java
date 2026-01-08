package com.qf.electronic.pojo;

import lombok.Data;

@Data
public class InspectionReception {

    private Long id;

    private Long taskId;

    private String taskName;

    private String lineId;

    private String lineName;

    private Integer defectTypeId;

    private String defectTypeName;

    private Integer defectLevelId;

    private String defectLevelName;

    private int state;

    private String stateText;

    private String rate;

    private String operationUser;

    private String operationUserName;
}

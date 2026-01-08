package com.qf.electronic.pojo;

import lombok.Data;

@Data
public class InspectionTask {

    private Long id;

    private String name;

    private String startNumber;

    private String endNumber;

    private String lineId;

    private String lineName;

    private int state;

    private String stateText;

    private String remark;

    private String inspectionUser;

    private String inspectionUserName;
}

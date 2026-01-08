package com.qf.electronic.pojo;

import lombok.Data;

@Data
public class DefectReception {

    private Long id;

    private Long taskId;

    private String taskName;

    private String lineId;

    private String lineName;

    private String finishInfo;

    private String pauseRecord;

    private String report;

    private int state;

    private String stateText;

    private String defectUser;

    private String defectUserName;
}

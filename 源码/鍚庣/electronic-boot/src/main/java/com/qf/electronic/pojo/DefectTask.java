package com.qf.electronic.pojo;

import lombok.Data;

@Data
public class DefectTask {

    private Long id;

    private String name;

    private String lineId;

    private String lineName;

    private String description;

    private String remark;

    private int state;

    private String stateText;

    private String defectUser;

    private String defectUserName;
}

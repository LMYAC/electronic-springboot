package com.qf.electronic.pojo;

import lombok.Data;

@Data
public class Role {

    private Integer id;

    private String name;

    private String enName;

    private int enableStatus;

    private String enableStatusText;
}

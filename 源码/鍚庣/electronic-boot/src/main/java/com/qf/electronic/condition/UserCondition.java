package com.qf.electronic.condition;

import lombok.Data;

@Data
public class UserCondition  extends Condition{

    private String username;

    private String name;

    private String phone;

    private Integer state;
}

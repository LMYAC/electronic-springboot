package com.qf.electronic.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private String id;

    private String url;

    private String method;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    private String user;

    private String userName;

    private String hasError;
}

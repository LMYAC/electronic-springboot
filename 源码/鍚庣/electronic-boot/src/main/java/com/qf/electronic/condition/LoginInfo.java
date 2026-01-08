package com.qf.electronic.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class LoginInfo {

    @ApiModelProperty(value = "账号", dataType = "java.lang.String", required=true, example = "admin")
    private String username;

    @ApiModelProperty(value = "密码", dataType = "java.lang.String", required=true, example = "123456")
    private String password;
}

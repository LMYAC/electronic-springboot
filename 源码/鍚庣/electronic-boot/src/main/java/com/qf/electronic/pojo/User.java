package com.qf.electronic.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class User implements UserDetails {

    @ExcelProperty(value = "账号", index = 0)
    private String username;

    private String password;

    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "性别", index = 2)
    private int sex;

    private String sexText;

    @ExcelProperty(value = "年龄", index = 3)
    private int age;

    @ExcelProperty(value = "联系电话", index = 4)
    private String phone;

    @ExcelProperty(value = "邮箱", index = 5)
    private String email;

    private int state;

    private String stateText;

    //FastJson提供了转换日期格式的注解，这个注解就是@JSONField
    @JSONField(format = "yyyy-MM-dd")
    @ExcelProperty(value = "入职时间", index = 6)
    private Date entryTime;

    @JSONField(format = "yyyy-MM-dd")
    private Date leaveTime;

    @JSONField(format = "yyyy-MM-dd")
    private Date createdTime;

    @ExcelProperty(value = "角色", index = 7)
    private Integer roleId;

    private String roleName;

    private List<GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return state == 1;
    }
}

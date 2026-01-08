package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LoginInfo;
import com.qf.electronic.condition.UserCondition;
import com.qf.electronic.condition.UserItemCondition;
import com.qf.electronic.pojo.User;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.UserService;
import com.qf.electronic.util.ExcelUtil;
import com.qf.electronic.util.SmsSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口文档")
public class UserController {

    @Autowired
    private UserService userService;

    //因为才用的是前后端分离的开发模式，因此前端访问的地址和后端访问的地址不一样，如果前端要与后端通信，就会造成由一个服务器到另一个服务器的请求访问，
    //这种情况称为跨域（http://localhost:7000 => http://localhost:8080），跨域需要在后端进行处理，主要是针对响应头进行处理，这种处理方式
    //需要进行后端编写代码处理（采用的过滤器的方式试下），带来了很多不变。因此我们可以通过反向代理来完成请求的交互，反向代理主要是在前端进行配置
    @GetMapping("/code/{mobile}")
    public void sendSmsCode(@PathVariable("mobile") String mobile, HttpSession session){
        SmsSender.sendSms(mobile, session);
    }

    //凡事请求参数在URL地址栏中的，取数据时，可以直接使用一个对象进行值的注入，也可以使用@RequestParam指定参数名进行取值
    //凡事请求参数在请求体中，取数据时，必须使用@RqeustBody来进行取值
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Integer> addUser(@RequestBody Map<String,Object> params){
        return Result.ok(userService.addUser(params));
    }

    @GetMapping("/init")
    public Result<Map<String,Object>> init(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return Result.ok(userService.getInitInfo(user));
    }

    @PutMapping("/logout")
    public Result<Boolean> logout(HttpSession session){
        //让session失效
        session.invalidate();
        return Result.ok(true);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<PageInfo<User>> searchUsers(UserCondition userCondition){
        return Result.ok(userService.searchUsers(userCondition));
    }

    @GetMapping("/{username}") //添加用户的时候检测用户名
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Boolean> existUser(@PathVariable("username")String username){
        boolean exists = true;
        try {
            userService.loadUserByUsername(username);
        } catch (Exception e){
            exists = false;
        }
        return Result.ok(exists);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Integer> deleteUser(@RequestBody Map<String,Object> params){
        List<String> usernames = (List<String>) params.get("usernames");
        return Result.ok(userService.deleteUser(usernames));
    }

    @PutMapping("/state")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Integer> changeUserState(@RequestBody Map<String,Object> params){
        return Result.ok(userService.changeUserState(params));
    }

    @PutMapping
    public Result<Integer> updateUser(@RequestBody Map<String,Object> params){
        return Result.ok(userService.updateUser(params));
    }

    @GetMapping("/condition/export")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportUser(UserCondition userCondition, HttpServletResponse response) throws Exception {
        List<User> users = userService.getExportUsers(userCondition);
        //设置响应头响应的内容类型：设置为可执行文件类型
        response.addHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        //这里的中文直接使用会出现中文乱码，出现乱码的原因是浏览器默认编码格式姐ISO_8859_1，而我们使用的这个
        //中文字符串的编码是UTF-8，两种编码不一致，因此出现乱码
        String fileName = "用户信息.xlsx";
        //首先得到该字符串在UTF-8编码下的字节数据
        byte[] bytes = fileName.getBytes(StandardCharsets.UTF_8);
        //将字节数据在新的编码格式下重新构建
        fileName = new String(bytes, StandardCharsets.ISO_8859_1);
        //Content-Disposition属性的作用就是在IE会弹出保存的对话框
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
        //这个输出流能够将内容输出到页面上去
        ServletOutputStream os = response.getOutputStream();
        ExcelUtil.exportExcel("用户信息列表", os, User.class, users);
    }

    @PostMapping("/file/import")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Boolean> importUsers(@RequestPart("files")MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        boolean result = ExcelUtil.importExcel(is, User.class, userService);
        return Result.ok(result);
    }

    @PutMapping("/role/dispatch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Integer> dispatchRole(@RequestBody Map<String,Object> params){
        String username = (String) params.get("username");
        Integer roleId = (Integer) params.get("roleId");
        return Result.ok(userService.updateUserRole(username, roleId));
    }

    @GetMapping("/item/query")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<PageInfo<User>> getUserItems(UserItemCondition userItemCondition){
        return Result.ok(userService.getUserItems(userItemCondition));
    }
}

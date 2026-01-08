package com.qf.electronic.aspect;

import com.qf.electronic.condition.LoginInfo;
import com.qf.electronic.pojo.Log;
import com.qf.electronic.service.LogService;
import com.qf.electronic.util.IdGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
public class LogAspect {

    @Autowired
    private LogService logService;
    /**
     * @Pointcut就是用来定义切点的，切点多了就形成了切面
     */
    @Pointcut("execution(* com.qf.electronic.controller..*(..))")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();//方法执行的参数
        //获取方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //判断是否是处理请求的方法
        if(isRequestMethod(method)){
            //先获取方法所在的类，然后再获取这个类的名称
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();
            String operationMethod = className + "." + methodName;
            //从请求上下文持有者中获取请求属性，因为我们使用的是Http请求，因此这个上下文持有者持有的就是
            //一个ServletRequestAttributes
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = sra.getRequest();
            //获取请求地址，因为请求地址中携带有上下文路径，因此需要将上下文件路径替换为空
            String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("user");
            String hasError = "不存在";
            try {
                return pjp.proceed();
            } catch (Throwable t) {
                hasError = "存在";
                throw t;
            } finally {
                Log log = new Log(IdGenerator.generateId("EG"), requestUrl, operationMethod, new Date(), user, null, hasError);
                if("/user/login".equals(requestUrl)){
                    LoginInfo info = (LoginInfo) args[0];
                    log.setUser(info.getUsername());
                }
                logService.addLog(log);
            }
        } else {
            return pjp.proceed();
        }
    }

    private boolean isRequestMethod(Method method){
        //method.isAnnotationPresent表示方法上是否存在某个注解
        return method.isAnnotationPresent(GetMapping.class)
                || method.isAnnotationPresent(PostMapping.class)
                || method.isAnnotationPresent(PutMapping.class)
                || method.isAnnotationPresent(DeleteMapping.class)
                || method.isAnnotationPresent(RequestMapping.class);
    }
}

package com.dycn.shairporthealth.exception;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Liq
 * @date 2021/4/1
 */

@ControllerAdvice//控制器增强
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);



    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exception(Exception exception){
        exception.printStackTrace();
        //记录日志
        LOGGER.error("catch exception:{}", Throwables.getStackTraceAsString(exception));
//        if(EXCEPTIONS == null){
//            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
//        }
//        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
//        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
//        if(resultCode !=null){
//            return new ResponseResult(resultCode);
//        }else{
//            //返回99999异常
//            return new ResponseResult(CommonCode.SERVER_ERROR);
//        }

        return "访问拒绝";


    }
}

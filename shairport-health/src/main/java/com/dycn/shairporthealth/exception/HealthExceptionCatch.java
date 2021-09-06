package com.dycn.shairporthealth.exception;

import com.dycn.shairportcommon.constant.ApiResponse;
import com.dycn.shairportcommon.constant.Status;
import com.dycn.shairportcommon.exception.ExceptionControllerAdvice;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Liq
 * @date 2021/4/1
 */

@RestControllerAdvice
public class HealthExceptionCatch extends ExceptionControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthExceptionCatch.class);



    /**
     * 处理AccessDeineHandler无权限异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public ApiResponse exceptionHandler(HttpServletRequest req, AccessDeniedException e){
        return ApiResponse.ofStatus(Status.ACCESS_DENIED);

    }
}

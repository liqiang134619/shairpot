package com.dycn.shairportcommon.exception;

import com.alibaba.fastjson.JSON;
import com.dycn.shairportcommon.constant.ApiResponse;
import com.dycn.shairportcommon.constant.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常统一处理
 * @author liq
 * @date 2020/2/3
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {






    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ApiResponse handleParamsException(Exception e) {

        BindingResult bindingResult = null;
        if(e instanceof BindException){
            BindException bindException = (BindException)e;
            bindingResult = bindException.getBindingResult();
        }else if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException)e;
            bindingResult = methodArgumentNotValidException.getBindingResult();
        }
        String errorMsg = e.getMessage();
        if(bindingResult!=null&&bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> fieldErrorMaps = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
            log.error("method arguments valid fails, fieldErrors : {}, e : {}", fieldErrorMaps, e);
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), JSON.toJSONString(fieldErrorMaps), fieldErrorMaps);
        }else{
            log.error("method arguments valid fails, e : {}", e);
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), errorMsg, errorMsg);
        }

    }


    /**
     * 处理AccessDeineHandler无权限异常
     * @param
     * @param
     * @return
     */
//    @ExceptionHandler(value = AccessDeniedException.class)
//    @ResponseBody
//    public ApiResponse exceptionHandler(HttpServletRequest req, AccessDeniedException e){
//        return ApiResponse.ofStatus(Status.ACCESS_DENIED);
//
//    }


    @ExceptionHandler(value = {Exception.class})
    public ApiResponse exceptionHandler(Exception exception, HttpServletResponse response) {
        // 自定义异常返回
//        if(exception instanceof CommonException) {
//            return ApiResponse.ofException((CommonException)exception);
//        }
        if(exception instanceof HttpMessageNotReadableException) {
            exception.printStackTrace();
            return ApiResponse.ofStatus(Status.PARAMS_ERROR);
        }



        // 其他异常返回
        log.error("【==>  ops   :{},{},{},{},{}】",exception.getMessage(),exception.getClass(),exception.getCause(),
                exception.getLocalizedMessage(),exception);
        return ApiResponse.ofStatus(Status.SERVER_ERROR);
    }






}

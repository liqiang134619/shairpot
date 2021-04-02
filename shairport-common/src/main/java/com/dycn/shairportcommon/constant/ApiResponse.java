package com.dycn.shairportcommon.constant;

import com.dycn.shairportcommon.exception.CommonException;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liq
 * @date 2020/2/5
 */
@Data
@SuppressWarnings("unchecked")
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 7148507549849361739L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回内容
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 分页总条数
     */
    private long total;

    /**
     * 无参构造函数
     */
    private ApiResponse() {

    }

    /**
     * 全参构造函数
     *
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     */
    private ApiResponse(Integer code, String message, T data, long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    /**
     * 构造一个自定义的API返回
     *
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     * @param total  分页记录数
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> of(Integer code, String message, T data, long total) {
        return new ApiResponse(code, message, data,total);
    }

    /**
     * 构造一个自定义的API返回
     *
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> of(Integer code, String message, T data) {
        return new ApiResponse(code, message, data, 0L);
    }

    public static <T> ApiResponse<T> ofSuccess(T data, String message, long total) {
        return new ApiResponse(Status.SUCCESS.getCode(), message, data, total);
    }

    /**
     * 构造一个成功且不带数据的API返回
     *
     * @return ApiResponse
     */
    public static ApiResponse ofSuccess() {
        return ofSuccess(Constant.EMPTY_STR);
    }

    public  static <T> ApiResponse ofFail(T data, String message) {
        return new ApiResponse(Status.SERVER_ERROR.getCode(), message, data, 0L);
    }



    public  static <T> ApiResponse ofFailed(T data, String message) {
        return new ApiResponse(Status.FAILED.getCode(), message, data, 0L);
    }

    public  static <T> ApiResponse ofFailed() {
        return ApiResponse.ofStatus(Status.FAILED);
    }


    /**
     * 构造一个成功且带数据的API返回
     *
     * @param data 返回数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> ofSuccess(T data) {
        return ofStatus(Status.SUCCESS, data);
    }

    /**
     * 构造一个成功且带数据
     *
     * @param data 返回数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> ofSuccess(T data, String message) {
        return new ApiResponse(Status.SUCCESS.getCode(), message, data, 0L);
    }

    /**
     * 构造一个成功且带数据的API返回及分页总条数
     *
     * @param data 返回数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> ofSuccess(T data,long total) {
        return ofStatus(data,total);
    }



    /**
     * 构造一个有状态的API返回
     *
     * @param status 状态 {@link Status}
     * @return ApiResponse
     */
    public static ApiResponse ofStatus(Status status) {
        return ofStatus(status, null);
    }

    /**
     * 构造一个有状态且带数据的API返回
     *
     * @param status 状态
     * @param data   返回数据
     * @return ApiResponse
     */
    private static <T> ApiResponse<T> ofStatus(Status status, T data) {
        return of(status.getCode(), status.getMsg(), data, 0L);
    }

    private static <T> ApiResponse<T> ofStatus(T data, long total) {
        return of(Status.SUCCESS.getCode(), Status.SUCCESS.getMsg(), data,total);
    }

    /**
     * 构造一个异常的API返回
     *
     * @param t   异常
     * @param <T> 异常
     * @return ApiResponse
     */
    public static <T extends CommonException> ApiResponse ofException(T t) {
        return of(t.getCode(), t.getMessage(),Constant.EMPTY_STR, 0L);
    }


}

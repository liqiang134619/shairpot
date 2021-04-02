package com.dycn.shairportcommon.constant;

/**
 * @author Liq
 * @date 2021/4/2
 */
public class ResponseCode {

    private ResponseCode() {}

    /**
     * Success
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * Failure
     * 失败
     */
    public static final int FAILURE = 500;

    /**
     * Bad Request
     * 请求错误
     */
    public static final int BAD_REQUEST = 400;

    /**
     * Unauthorized
     * 未认证
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * Forbidden
     * 无权限
     */
    public static final int FORBIDDEN = 403;

    /**
     * Not Found
     * 请求不存在
     */
    public static final int NOT_FOUND = 404;

    /**
     * Method Not Allowed
     * 方法不允许
     */
    public static final int METHOD_NOT_ALLOWED = 405;

    /**
     * Request Timeout
     * 请求超时
     */
    public static final int REQUEST_TIMEOUT = 408;

    /**
     * Too Many Requests
     * 请求太多
     */
    public static final int TOO_MANY_REQUESTS = 429;
}

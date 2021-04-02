package com.dycn.shairportcommon.constant;

/**
 * @author liq
 * @date 2020/2/3
 */

public enum Status {


    /**
     * 通用状态参数
     */
    SUCCESS(200, "接口调用成功"),
    FAILED(300, "接口调用失败"),
    NO_RESOURCE(301, "资源不存在"),
    SERVER_ERROR(500, "服务器开小差~"),
    PARAMS_ERROR(501, "请正确填写参数~"),
    ACCESS_TOKEN_NOT_FOUND(903, " token"),


    // 认证相关
    UNAUTHORIZED(401, "请先登录！"),
    ACCESS_DENIED(403, "权限不足！"),
    USER_NOT_FOUND(407, "用户名或密码错误！"),
    USER_DISABLED(408, "账号已禁用！"),
    LOGOUT(200, "退出成功！"),
    LOGIN_FAILED(801, "登录失败"),
    LOGIN_USER_NOTFOUND(802, "用户不存在"),
    LOGIN_USER_FORBIDDEN(802, "用户无权限，无法登录。"),


    ORG_FORBIDDEN(701, "单位名称冲突"),
    AREA_FORBIDDEN(702, "区域名称冲突"),


    FACE_IMG_ERROR(603, "原始图片无特征或格式错误"),


    // token 相关
    TOKEN_EXPIRED(901, "token无效");


    private int code;
    private String msg;

    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}


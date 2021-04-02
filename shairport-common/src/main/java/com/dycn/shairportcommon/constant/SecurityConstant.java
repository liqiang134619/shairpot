package com.dycn.shairportcommon.constant;

/**
 * @author Liq
 * @date 2021/4/2
 */
public class SecurityConstant {


    /**
     * 登录url
     */
    public static final String LOGIN_URL = "/auth/login";

    /**
     * 认证url
     */
    public static final String OAUTH_URL = "/oauth/";

    /**
     * token 名称
     */
    public static final String ACCESS_TOKEN = "Authorization";

    /**
     * bearer 前缀
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * access 前缀
     */
    public static final String ACCESS_PREFIX = "access:";


    /**
     * 系统固定不进行认证，直接放行的URL，供WebSecurityConfig、ResourceServerConfig公用
     */
    public static final String[] PATTERN_URLS = {
            "/actuator/**",
            "/druid/**",

            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/swagger-ui.html",
            "/doc.html"
    };
}

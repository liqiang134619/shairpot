package com.dycn.shairporthealth.exception;

import com.dycn.shairportcommon.constant.Status;
import com.dycn.shairportcommon.util.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liq
 * @date 2021-9-8
 */
public class CustomAccessDeniedHandler  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResponseUtil.renderJson(response, Status.ACCESS_DENIED);

    }
}

package com.dycn.shairportexptimpt.config.controller;

import com.dycn.shairportcommon.constant.ApiResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liq
 * @date 2021-9-13
 */
@RestController
@RequestMapping("/export")
@Api(tags = "导入导出服务")
public class BaseController {


    @GetMapping("/test")
    public ApiResponse baseTest() {
        return ApiResponse.ofSuccess();
    }


}

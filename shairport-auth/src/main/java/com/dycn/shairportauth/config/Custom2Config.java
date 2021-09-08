package com.dycn.shairportauth.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liq
 * @date 2021/1/22
 */

@ConfigurationProperties(prefix = "custom.config")
@Data
public class Custom2Config {

    private List<String> ignores = new ArrayList<>();


    //特殊角色
    private List<String> specialRoles = Lists.newArrayList();


    //默认角色
    private List<String> defaultRoles = Lists.newArrayList();
}

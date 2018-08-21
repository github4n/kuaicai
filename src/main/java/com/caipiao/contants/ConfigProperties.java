package com.caipiao.contants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjj
 * @create 2018-08-13 13:41
 **/
@Configuration
@ConfigurationProperties(prefix="props")
@Getter
@Setter
public class ConfigProperties {

    private String gd11x5Url;
}

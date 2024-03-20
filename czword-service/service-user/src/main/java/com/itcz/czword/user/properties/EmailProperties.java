package com.itcz.czword.user.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private String username;
    private String password;
}

package com.eceris.replica.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "db.replica")
@Getter
@Setter
public class DbProperties {

    private String url;
    private String username;
    private String password;
    private List<Slave> slaves;

    @Getter
    @Setter
    public static class Slave {
        private String name;
        private String url;
    }
}

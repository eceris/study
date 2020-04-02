package com.eceris.shard.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "db.sharding")
@Getter
@Setter
public class DbProperties {

    private String username;
    private String password;
    private String namesPrefix;
    private String defaultColumn;
    private List<Shard> shards;
    private List<String> tables;
    private List<String> columns;

    @Getter
    @Setter
    public static class Shard {
        private String name;
        private String url;
    }

    private boolean autoReconnect = true;
    private boolean cacheServerConfiguration = true;
    private boolean useLocalSessionState = true;
    private boolean elideSetAutoCommits = true;
    private int connectTimeout = 3000;
    private int socketTimeout = 60000;
    private boolean useSSL = false;
    private boolean useAffectedRows = true;
    private boolean useUnicode = true;
    private String characterEncoding = "UTF-8";
    private String ddlAuto = "none";
}

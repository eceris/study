package com.eceris.shard.config;

import com.zaxxer.hikari.HikariDataSource;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@PropertySources(
        @PropertySource("classpath:db/ro.properties")
)
//@EnableJpaRepositories(
//        basePackages = {"com.eceris.shard"},
//        entityManagerFactoryRef = "shardEntityManager",
//        transactionManagerRef = "shardTransactionManager"
//)
public class ReadShardDbConfig {

    private final ReadShardDbProperties readShardDbproperties;

    private HikariDataSource toHikariDataSource(Integer index) {

        HikariDataSource hikariDataSource = new HikariDataSource();
        ReadShardDbProperties.Shard shard = readShardDbproperties.getShards().get(index);
        hikariDataSource.setJdbcUrl(shard.getUrl());
        hikariDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        hikariDataSource.setUsername(readShardDbproperties.getUsername());
        hikariDataSource.setPassword(readShardDbproperties.getPassword());
        hikariDataSource.setConnectionTestQuery("SELECT 1");

        hikariDataSource.setMaximumPoolSize(16);
        hikariDataSource.setMinimumIdle(8);
        hikariDataSource.setPoolName("shardPool_" + index.toString());
        hikariDataSource.addDataSourceProperty("autoReconnect", readShardDbproperties.isAutoReconnect());
        hikariDataSource.addDataSourceProperty("cacheServerConfiguration", readShardDbproperties.isCacheServerConfiguration());
        hikariDataSource.addDataSourceProperty("useLocalSessionState", readShardDbproperties.isUseLocalSessionState());
        hikariDataSource.addDataSourceProperty("elideSetAutoCommits", readShardDbproperties.isElideSetAutoCommits());
        hikariDataSource.addDataSourceProperty("connectTimeout", readShardDbproperties.getConnectTimeout());
        hikariDataSource.addDataSourceProperty("socketTimeout", readShardDbproperties.getSocketTimeout());
        hikariDataSource.addDataSourceProperty("useSSL", readShardDbproperties.isUseSSL());
        hikariDataSource.addDataSourceProperty("useAffectedRows", readShardDbproperties.isUseAffectedRows());
        hikariDataSource.addDataSourceProperty("useUnicode", readShardDbproperties.isUseUnicode());
        hikariDataSource.addDataSourceProperty("characterEncoding", readShardDbproperties.getCharacterEncoding());
        return hikariDataSource;
    }


    private static String toShardNumber(Integer shardIndex) {
//        String s = shardIndex.toString();
//        if (s.length() == 2) return s;
//        return "0" + s;
        return shardIndex.toString();
    }

//    @Bean
//    public PlatformTransactionManager shardTransactionManager(DataSource readShardDataSource) {
//        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//        jpaTransactionManager.setEntityManagerFactory(shardEntityManager(readShardDataSource).getObject());
//        return jpaTransactionManager;
//    }
//
//    @Bean
//    LocalContainerEntityManagerFactoryBean shardEntityManager(DataSource readShardDataSource) {
//
//        HashMap<String, Object> propertyMap = Maps.newHashMap();
//        propertyMap.put("show_sql", true);
//        propertyMap.put("hibernate.hbm2ddl.auto", "none");
//        propertyMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
//        propertyMap.put("hibernate.ejb.entitymanager_factory_name", "shardEntityManager");
//        propertyMap.put("hibernate.jdbc.batch_size", 500);
//        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//        localContainerEntityManagerFactoryBean.setDataSource(readShardDataSource);
//        localContainerEntityManagerFactoryBean.setPersistenceUnitName("shardEntityUnit");
//        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        localContainerEntityManagerFactoryBean.setPackagesToScan("com.eceris.shard");
//        localContainerEntityManagerFactoryBean.setJpaPropertyMap(propertyMap);
//        return localContainerEntityManagerFactoryBean;
//    }

    @Bean
    public DataSource readShardDataSource() throws SQLException {
        Map<String, DataSource> dataSources = IntStream
                .range(0, readShardDbproperties.getShards().size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> readShardDbproperties.getShards().get(i).getName(),
                        this::toHikariDataSource
                ));
        Properties properties = new Properties();
        properties.setProperty("sql.show", String.valueOf(true));
        return ShardingDataSourceFactory.createDataSource(
                dataSources,
                getDBShardingRuleConfiguration(),
                new HashMap<>(),
                properties
        );
    }

    // Configure Sharding Rule
    private ShardingRuleConfiguration getDBShardingRuleConfiguration() {
        final PreciseShardingAlgorithm<String> shardingAlgorithm = (availableTargetNames, shardingValue) -> {
            final Integer shardIndex = Math.abs((shardingValue.getValue().hashCode() % readShardDbproperties.getShards().size()));
            return availableTargetNames.stream()
                    .filter(x -> x.endsWith(toShardNumber(shardIndex + 1))) // TODO
                    .findFirst().orElse(null);
        };
        StandardShardingStrategyConfiguration defaultStrategyConfig = new StandardShardingStrategyConfiguration(readShardDbproperties.getDefaultColumn(), shardingAlgorithm);
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.setTableRuleConfigs(getTableRuleConfigurations(shardingAlgorithm));
        shardingRuleConfiguration.setBindingTableGroups(readShardDbproperties.getTables());
        shardingRuleConfiguration.setDefaultDatabaseShardingStrategyConfig(defaultStrategyConfig);
        return shardingRuleConfiguration;
    }

    // Configure Table Rule
    private List<TableRuleConfiguration> getTableRuleConfigurations(PreciseShardingAlgorithm<String> shardingAlgorithm) {
        List<Integer> tableIndices = IntStream.range(0, readShardDbproperties.getTables().size())
                .boxed()
                .collect(Collectors.toList());
        List<Integer> dbIndices = IntStream.range(0, readShardDbproperties.getShards().size())
                .boxed()
                .collect(Collectors.toList());
        return tableIndices.stream().map(tableIndex -> {
            String logicTable = readShardDbproperties.getTables().get(tableIndex);
            String actualDataNodes = dbIndices.stream().map(shardIndex -> {
                final String prefix = readShardDbproperties.getNamesPrefix();
                String shardNumberPadded = toShardNumber(shardIndex + 1);
                return new StringBuilder()
                        .append(prefix).append(shardNumberPadded)
                        .append(".").append(logicTable);
            }).collect(Collectors.joining(","));
            StandardShardingStrategyConfiguration strategyConfig = new StandardShardingStrategyConfiguration(
                    readShardDbproperties.getColumns().get(tableIndex),
                    shardingAlgorithm
            );
            TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
            tableRuleConfiguration.setLogicTable(logicTable);
            tableRuleConfiguration.setActualDataNodes(actualDataNodes);
            tableRuleConfiguration.setDatabaseShardingStrategyConfig(strategyConfig);
            return tableRuleConfiguration;
        }).collect(Collectors.toList());
    }
}

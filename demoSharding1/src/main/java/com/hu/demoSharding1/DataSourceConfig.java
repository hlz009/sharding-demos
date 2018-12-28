package com.hu.demoSharding1;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;

@Configuration
public class DataSourceConfig {

	@Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.maxWait}")
    private int maxWait;

//    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
//    private int timeBetweenEvictionRunsMillis;
//
//    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${spring.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${spring.datasource.testWhileIdle}")
//    private boolean testWhileIdle;
//
//    @Value("${spring.datasource.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Value("${spring.datasource.testOnReturn}")
//    private boolean testOnReturn;
//
//    @Value("${spring.datasource.poolPreparedStatements}")
//    private boolean poolPreparedStatements;
//
//    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
//    private int maxPoolPreparedStatementPerConnectionSize;
//
//    @Value("${spring.datasource.filters}")
//    private String filters;
//
//    @Value("{spring.datasource.connectionProperties}")
//    private String connectionProperties;

	@Bean
	public DataSource getDataSource() throws SQLException {
		return buildDataSource();
	}

	private DataSource buildDataSource() throws SQLException {
		//设置分库映射
		Map<String, DataSource> dataSourceMap = new HashMap<>(2);
		//添加两个数据库ds_0,ds_1到map里
		dataSourceMap.put("ds_0", createDataSource("ds_0"));
		dataSourceMap.put("ds_1", createDataSource("ds_1"));
		TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
		orderTableRuleConfig.setLogicTable("t_order");
		orderTableRuleConfig.setDatabaseShardingStrategyConfig(new 
				InlineShardingStrategyConfiguration("user_id", "ds_${user_id % 2}"));

	    ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
	    shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
	    DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig,
	    		new ConcurrentHashMap(), new Properties());
		return dataSource;
	}

	private DataSource createDataSource(String dataSourceName) {
        //使用druid连接数据库
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(Driver.class.getName());
        datasource.setUrl(String.format(dbUrl + "%s", dataSourceName));
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        // configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        System.out.println("已加载---" + dataSourceName);
        return datasource;
    }

}

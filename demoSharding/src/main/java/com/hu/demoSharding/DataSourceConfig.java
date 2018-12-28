package com.hu.demoSharding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.hu.demoSharding.ruler.ModuloDatabaseShardingAlgorithm;
import com.mysql.jdbc.Driver;

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

	@Bean
	public DataSource getDataSource() {
		return buildDataSource();
	}

	private DataSource buildDataSource() {
		//设置分库映射
		Map<String, DataSource> dataSourceMap = new HashMap<>(2);
		//添加两个数据库ds_0,ds_1到map里
		dataSourceMap.put("ds_0", createDataSource("ds_0"));
		dataSourceMap.put("ds_1", createDataSource("ds_1"));
		//设置默认db为ds_0，也就是为那些没有配置分库分表策略的指定的默认库
        //如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据
		DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "ds_0");
		TableRule orderTableRule = TableRule.builder("t_order")
				.dataSourceRule(dataSourceRule)
				.build();
//		//设置分表映射，将t_order_0和t_order_1两个实际的表映射到t_order逻辑表
//        //0和1两个表是真实的表，t_order是个虚拟不存在的表，只是供使用。如查询所有数据就是select * from t_order就能查完0和1表的
//        TableRule orderTableRule = TableRule.builder("t_order")
//                .actualTables(Arrays.asList("t_order_0", "t_order_1"))
//                .dataSourceRule(dataSourceRule)
//                .build();
		ShardingRule shardingRule = ShardingRule.builder()
				.dataSourceRule(dataSourceRule)
				.tableRules(Arrays.asList(orderTableRule))
				.databaseShardingStrategy(new DatabaseShardingStrategy("user_id",
						new ModuloDatabaseShardingAlgorithm()))
				.build();
				//还可以在增加分表名的策略
		DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
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

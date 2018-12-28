package com.hu.demoSharding.ruler;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

/**
 * 分库分表规则
 * @author xiaozhi009
 *
 */
public class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long>{

	static {
		System.out.println("初始化加载数据库分片规则");
	}

	@Override
	public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
		for (String targetName : availableTargetNames) {
			if (targetName.endsWith(hash(shardingValue.getValue()))) {
				return targetName;
			}
		}
		throw new IllegalArgumentException("找不到插入的数据库");
	}

	@Override
	public Collection<String> doInSharding(Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (Long value : shardingValue.getValues()) {
            for (String tableName : availableTargetNames) {
                if (tableName.endsWith(hash(value))) {
                    result.add(tableName);
                }
            }
        }
        return result;
	}

	@Override
	public Collection<String> doBetweenSharding(Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : availableTargetNames) {
                if (each.endsWith(hash(range))) {
                    result.add(each);
                }
            }
        }
        return result;
	}

	private String hash(Object obj) {
		int hash = Math.abs(obj.hashCode() % 1024);
		return String.valueOf(hash % 2);
	}
}

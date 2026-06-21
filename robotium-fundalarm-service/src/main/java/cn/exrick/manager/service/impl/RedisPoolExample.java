package cn.exrick.manager.service.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolExample {

	// 连接池配置（通常作为静态成员或单例）
	private static JedisPool jedisPool;

	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100); // 最大连接数
		config.setMaxIdle(20); // 最大空闲连接
		config.setMinIdle(5); // 最小空闲连接
		config.setTestOnBorrow(true); // 借用时测试连接
		config.setTestOnReturn(true); // 归还时测试连接
		config.setTestWhileIdle(true); // 空闲时测试连接
		config.setMaxWaitMillis(3000); // 获取连接最大等待3秒
		config.setTimeBetweenEvictionRunsMillis(30000); // 每30秒检测一次空闲连接

		jedisPool = new JedisPool(config, "localhost", // Redis服务器地址
				6379, // 端口
				3000, // 连接超时(ms)
				null, // 密码（无密码填null）
				4 // 数据库索引
		);
	}

	// 获取Jedis实例（用完必须关闭）
	public static Jedis getJedis() {
		return jedisPool.getResource();
	}

	// 封装常用操作
	public static String get(String key) {
		try (Jedis jedis = getJedis()) {
			return jedis.get(key);
		}
	}

	public static String brpoplpush(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.brpoplpush(key, key + "_bak", 5);
		} finally {
			if (jedis != null) {
				try {
					jedis.close();
				} catch (Exception e) {
					// ignore close error
				}
			}
		}
	}

	public static void set(String key, String value) {
		try (Jedis jedis = getJedis()) {
			jedis.set(key, value);
		}
	}

	public static void lpush(String key, String value) {
		try (Jedis jedis = getJedis()) {
			jedis.lpush(key, value);
		}
	}

	public static void rpush(String key, String value) {
		try (Jedis jedis = getJedis()) {
			jedis.rpush(key, value);
		}
	}

	public static void lrem(String key, String value) {
		try (Jedis jedis = getJedis()) {
			jedis.lrem(key, 1, value);
		}
	}

	public static void setex(String key, int seconds, String value) {
		try (Jedis jedis = getJedis()) {
			jedis.setex(key, seconds, value);
		}
	}

	// 关闭连接池（应用关闭时调用）
	public static void close() {
		if (jedisPool != null && !jedisPool.isClosed()) {
			jedisPool.close();
		}
	}

	// 使用示例
	public static void main(String[] args) {
		set("test", "hello2");
		System.out.println(get("test"));

		// 应用关闭时
		// close();
	}
}
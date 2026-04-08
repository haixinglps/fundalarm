package cn.exrick.common.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

/**
 * @author Exrickx
 */
public class JedisClientCluster implements JedisClient {

	private JedisCluster jedisCluster;

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public Boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public Long expire(String key, int seconds) {
		return jedisCluster.expire(key, seconds);
	}

	@Override
	public Long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public Long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}

	@Override
	public Long hdel(String key, String... field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public Boolean hexists(String key, String field) {
		return jedisCluster.hexists(key, field);
	}

	@Override
	public List<String> hvals(String key) {
		return jedisCluster.hvals(key);
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public String setex(String key, Integer second, String value) {
		String result = jedisCluster.setex(key, second, value);
		return result;
	}

	@Override
	public long rpush(String key, String value) {
		// TODO 自动生成的方法存根
		long result = jedisCluster.rpush(key, value);

		return result;
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Long lpush(String key, String value) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Long lrem(String key, long count, String value) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String spop(String key) {
		String re = jedisCluster.spop(key);
		return re;
	}

	@Override
	public Set<String> spop(String key, int num) {
		// Jedis jedis = jedisCluster.getResource();
		Set<String> result = jedisCluster.spop(key, num);

		return result;
	}

	@Override
	public long sadd(String key, String value) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public Long scard(String key) {
		long re = jedisCluster.scard(key);
		return re;
	}

	@Override
	public List<String> brpop(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long zadd(String key, Double score, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String brpoplpushdemand(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long decr(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void watch(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public Transaction multi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unwatch(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public long delPostCountifZero(String posCountKey) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long zrem(String key, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<Tuple> ZREVRANGEBYSCORE(String key, double max, double min, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> zrange(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long delZanInfo(String userInfoKey, String posCountKey, int addtag, String status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long zrank(String key, String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long delZanInfo(String userInfoKey, String posCountKey, String userLikeSetKey, String userUnLinkKey,
			String postId, int addtag, String status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long setnx(String key, String value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String poll(String queueName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long incrBy(String key, long num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long delZanInfoForScore(String userInfoKey, String posCountKey, int addtag, String status, Long score) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> zrangeMinMax(String key, int direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double zscore(String key, String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long zcard(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ltrim(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Tuple> zrangeWithScore(String key, int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ltrim(String key, int begin, int end) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long llen(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lindex(String listKey, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}

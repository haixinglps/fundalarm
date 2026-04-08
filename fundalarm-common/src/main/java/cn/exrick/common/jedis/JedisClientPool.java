package cn.exrick.common.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

/**
 * @author Exrickx
 */
public class JedisClientPool extends RedisManager implements JedisClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(JedisClientPool.class);

	@Value("${database}")
	int database;
	@Value("${cachedatabase}")
	int cachedatabase;

	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
//		RedisCache<., V>
//		RedisCache.cachedatabase = cachedatabase;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	@Override
	public long setnx(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long result = jedis.setnx(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);
//		System.out.println("-----选择数据库-----" + database);

		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Boolean result = jedis.exists(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incrBy(String key, long num) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.incrBy(key, num);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.hset(key, field, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String result = jedis.hget(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.hdel(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Boolean hexists(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Boolean result = jedis.hexists(key, field);
		jedis.close();
		return result;
	}

	@Override
	public List<String> hvals(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		List<String> result = jedis.hvals(key);
		jedis.close();
		return result;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long result = jedis.del(key);
		jedis.close();
		return result;
	}

	@Override
	public String setex(String key, Integer second, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String result = jedis.setex(key, second, value);
		jedis.close();
		return result;
	}

	@Override
	public long rpush(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long result = jedis.rpush(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String ltrim(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String result = jedis.ltrim(key, 0, 4);
		jedis.close();
		return result;
	}

	/**
	 * 获取List列表
	 * 
	 * @param key
	 * @param start long，开始索引
	 * @param end   long， 结束索引
	 * @return List<String>
	 */
	@Override
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		List<String> list = jedis.lrange(key, start, end);
		jedis.close();
		return list;
	}

	@Override
	public Long llen(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long list = jedis.llen(key);
		jedis.close();
		return list;
	}

	@Override
	public String lindex(String key, int index) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String list = jedis.lindex(key, index);
		jedis.close();
		return list;
	}

	/**
	 * 将一个值插入到列表头部，value可以重复，返回列表的长度
	 * 
	 * @param key
	 * @param value String
	 * @return 返回List的长度
	 */
	@Override
	public Long lpush(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long length = jedis.lpush(key, value);
		jedis.close();
		return length;
	}

	@Override
	public void ltrim(String key, int begin, int end) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		jedis.ltrim(key, begin, end);
		jedis.close();
		return;
	}

	@Override
	public List<String> brpop(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		List<String> data = jedis.brpop(key);
		jedis.close();
		return data;
	}

	@Override
	public String brpoplpushdemand(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String data = jedis.brpoplpush(key, key + "_bak", 10);

		jedis.close();
		return data;
	}

	/**
	 * 移除列表元素，返回移除的元素数量
	 * 
	 * @param key
	 * @param count，标识，表示动作或者查找方向
	 *                            <li>当count=0时，移除所有匹配的元素；</li>
	 *                            <li>当count为负数时，移除方向是从尾到头；</li>
	 *                            <li>当count为正数时，移除方向是从头到尾；</li>
	 * @param value               匹配的元素
	 * @return Long
	 */
	@Override
	public Long lrem(String key, long count, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long length = jedis.lrem(key, count, value);
		jedis.close();
		return length;
	}

	@Override
	public Set<String> spop(String key, int num) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Set<String> result = jedis.spop(key, num);
		jedis.close();
		return result;
	}

	@Override
	public long sadd(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long size = 0;
		try {
			size = jedis.sadd(key, value);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return size;

	}

	@Override
	public long zadd(String key, Double score, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long size = 0;
		try {
			size = jedis.zadd(key, score, value);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return size;

	}

	@Override
	public long zrem(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long size = 0;
		try {
			size = jedis.zrem(key, value);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return size;

	}

	@Override
	public String spop(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		String re = jedis.spop(key);
		jedis.close();
		return re;
	}

	@Override
	public Long scard(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long re = jedis.scard(key);
		jedis.close();
		return re;
	}

	@Override
	public Long zcard(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		long re = jedis.zcard(key);
		jedis.close();
		return re;
	}

	@Override
	public byte[] get(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);
		byte[] value;

		try {
			value = jedis.get(key);
		} catch (Exception e) {
			LOGGER.error("redis key:{} get value occur exception", new String(key));
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}

		return value;
	}

	@Override
	public byte[] set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		try {
			jedis.set(key, value);
			Integer expire = getExpire();
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LOGGER.error("redis key:{} set value:{} occur exception", new String(key), new String(value));
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}

		return value;
	}

	@Override
	public byte[] set(byte[] key, byte[] value, int expire) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		try {
			jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			LOGGER.error("redis key:{} set value:{} in expire:{} occur exception", new String(key), new String(value),
					expire);
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}

		return value;
	}

	@Override
	public void del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		try {
			jedis.del(key);
		} catch (Exception e) {
			LOGGER.error("redis key:{} del value occur exception", new String(key));
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}
	}

	@Override
	public void flushDB() {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);
		try {
			jedis.flushDB();
		} catch (Exception e) {
			LOGGER.error("redis flushDB occur exception");
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}

	}

	@Override
	public Long dbSize() {
		Long dbSize = Long.valueOf(0L);
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		try {
			dbSize = jedis.dbSize();
		} catch (Exception e) {
			LOGGER.error("redis get dbSize occur exception");
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}

		return dbSize;
	}

	@Override
	public Set<byte[]> keys(String pattern) {
		Set keys = null;
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		try {
			keys = jedis.keys(pattern.getBytes());
		} catch (Exception e) {
			LOGGER.error("redis get keys in pattern:{} occur exception", pattern);
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}

		return keys;
	}

	@Override
	public Long geoadd(String key, double longitude, double latitude, String member) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);
		Long geoadd;
		try {
			geoadd = jedis.geoadd(key, longitude, latitude, member);
		} catch (Exception e) {
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}
		return geoadd;
	}

	@Override
	public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);
		Long geoadd;
		try {
			geoadd = jedis.geoadd(key, memberCoordinateMap);
		} catch (Exception e) {
			throw new RuntimeException("redis operation error:", e);
		} finally {
			jedis.close();
		}
		return geoadd;
	}

	@Override
	public Long decr(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long re = jedis.decr(key);
		jedis.close();
		return re;
	}

//	@Override
//	public void watch(String key) {
//		// TODO Auto-generated method stub
//		Jedis jedis = jedisPool.getResource();
//		jedis.select(database);
//		jedis.watch(key);
//
//	}

//	@Override
//	public Transaction multi() {
//		// TODO Auto-generated method stub
//		Jedis jedis = jedisPool.getResource();
//		jedis.select(database);
//		jedis.multi();
//		return null;
//	}

//	@Override
//	public void unwatch(String key) {
//		// TODO Auto-generated method stub
//		Jedis jedis = jedisPool.getResource();
//		jedis.select(database);
//		jedis.unwatch();
//		jedis.close();
//
//	}

	@Override
	public long delZanInfo(String userInfoKey, String posCountKey, String userLikeSetKey, String userUnLinkKey,
			String postId, int addtag, String status) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();

		int result = 0;
		try {
			jedis.select(database);
			jedis.watch(userInfoKey);
			String nowstatus = jedis.hget(userInfoKey, "status");
			if (!nowstatus.contentEquals(status)) {
				jedis.unwatch();
				return 0;
			}
			Transaction trans = jedis.multi();
			trans.del(userInfoKey);
			if (addtag == 1) {
				trans.incr(posCountKey);
				if (userUnLinkKey != null)
					trans.zrem(userUnLinkKey, postId);
			}

			else if (addtag == 0) {
				trans.decr(posCountKey);
				if (userLikeSetKey != null)
					trans.zrem(userLikeSetKey, postId);
			}
			List<Object> execResult = trans.exec();
			result = 0;
			if (CollectionUtils.isEmpty(execResult)) {
				System.out.println("刷新失败,用户可能在点赞");
				result = 0;
			} else {
				// 秒杀成功
				System.out.println("刷新成功");
				result = 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			result = 0;
		} finally {
			if (jedis != null)
				jedis.close();
		}

		return result;

	}

	@Override
	public long delZanInfo(String userInfoKey, String posCountKey, int addtag, String status) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		int result = 0;
		try {

			jedis.select(database);
			jedis.watch(userInfoKey);
			String nowstatus = jedis.hget(userInfoKey, "status");
			if (!nowstatus.contentEquals(status)) {
				jedis.unwatch();

				return 0;
			}

			Transaction trans = jedis.multi();
			trans.del(userInfoKey);

			if (addtag == 1) {
				trans.incr(posCountKey);

			}

			else if (addtag == 0) {
				trans.decr(posCountKey);

			}

			List<Object> execResult = trans.exec();

			if (CollectionUtils.isEmpty(execResult)) {
				System.out.println("刷新失败,用户可能在点赞");
				result = 0;
			} else {
				// 秒杀成功
				System.out.println("刷新成功");
				result = 1;
			}

		} catch (Exception e) {
			// TODO: handle exception
			result = 0;
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return result;

	}

	@Override
	public long delZanInfoForScore(String userInfoKey, String posCountKey, int addtag, String status, Long score) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		int result = 0;
		try {

			jedis.select(database);
			jedis.watch(userInfoKey);
			String nowstatus = jedis.hget(userInfoKey, "status");
			if (!nowstatus.contentEquals(status)) {
				jedis.unwatch();

				return 0;
			}

			Transaction trans = jedis.multi();
			trans.del(userInfoKey);

			if (addtag == 1) {
				trans.incrBy(posCountKey, score);

			}

			else if (addtag == 0) {
				trans.decrBy(posCountKey, score);

			}

			List<Object> execResult = trans.exec();

			if (CollectionUtils.isEmpty(execResult)) {
				System.out.println("刷新失败,用户可能在点赞");
				result = 0;
			} else {
				// 秒杀成功
				System.out.println("刷新成功");
				result = 1;
			}

		} catch (Exception e) {
			// TODO: handle exception
			result = 0;
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return result;

	}

	@Override
	public long delPostCountifZero(String posCountKey) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		int result = 0;
		try {

			jedis.select(database);

			jedis.watch(posCountKey);
			String cc = jedis.get(posCountKey);
			int ccvalue = Integer.parseInt(cc);
			if (ccvalue != 0) {
				jedis.unwatch();

				return 0;
			}
			Transaction trans = jedis.multi();
			trans.del(posCountKey);

			List<Object> execResult = trans.exec();

			if (CollectionUtils.isEmpty(execResult)) {
				System.out.println("刷新失败,用户可能在点赞");
				result = 0;
			} else {
				// 秒杀成功
				System.out.println("刷新成功");
				result = 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			result = 2;
		} finally {
			if (jedis != null)
				jedis.close();
		}

		return result;

	}

	@Override
	public Set<Tuple> ZREVRANGEBYSCORE(String key, double max, double min, int size) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Set<Tuple> posts = null;
		try {
			posts = jedis.zrevrangeByScoreWithScores(key, max, min, 0, size);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return posts;

	}

	@Override
	public Set<Tuple> zrangeWithScore(String key, int start, int end) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Set<Tuple> posts = null;
		try {
			posts = jedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return posts;

	}

	@Override
	public Set<String> zrange(String key) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Set<String> posts = null;
		try {
			posts = jedis.zrange(key, 0, -1);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return posts;

	}

	@Override
	public Double zscore(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Double posts = null;
		try {
			posts = jedis.zscore(key, member);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return posts;

	}

	@Override
	public Set<String> zrangeMinMax(String key, int direction) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Set<String> posts = null;
		try {
			if (direction == 0)
				posts = jedis.zrange(key, 0, 0);
			else {
				posts = jedis.zrevrange(key, 0, 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return posts;

	}

	@Override
	public Long zrank(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);

		Long posts = -1l;
		try {
			posts = jedis.zrank(key, member);

		} catch (Exception e) {
			// TODO: handle exception
		}
		jedis.close();
		return posts;

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
	public String poll(String queueName) {
		Jedis jedis = jedisPool.getResource();
		jedis.select(database);
		// 1. 取第一个元素
		Set<Tuple> o = jedis.zrangeWithScores(queueName, 0, 0);
		if (o.size() == 0) {
			jedis.close();
			return null;
		}
		// 2. 然后和当前时间比，是否已到了处理时间
		String result = null;
		try {
			Transaction trans = jedis.multi();
			for (Tuple tuple : o) {
				if (tuple.getScore() > System.currentTimeMillis())
					break;
				// 这里为了安全起，先拿到一个元素，放入备份队列之后再从原队列中删除
				// 3. 先放入备份队列

				trans.hset(queueName + "_bak", tuple.getElement(), tuple.getElement());
				// 4. 在从 zset 中删除
				trans.zrem(queueName, tuple.getElement());

				List<Object> execResult = trans.exec();

				if (CollectionUtils.isEmpty(execResult)) {
					System.out.println("获取zset头部元素失败");

				} else {
					// 秒杀成功
					System.out.println("获取zset头部元素成功");
					result = tuple.getElement();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (jedis != null)
				jedis.close();
		}

		return result;

	}

}
//test 8
//pro 8

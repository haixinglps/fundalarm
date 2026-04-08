package cn.exrick.common.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

/**
 * @author Exrickx
 */
public interface JedisClient {

	String set(String key, String value);

	String setex(String key, Integer second, String value);

	long rpush(String key, String value);

	String get(String key);

	Boolean exists(String key);

	Long expire(String key, int seconds);

	Long ttl(String key);

	Long incr(String key);

	Long hset(String key, String field, String value);

	String hget(String key, String field);

	Long hdel(String key, String... field);

	Boolean hexists(String key, String field);

	List<String> hvals(String key);

	Long del(String key);

	List<String> lrange(String key, long start, long end);

	Long lpush(String key, String value);

	List<String> brpop(String key);

	Long lrem(String key, long count, String value);

	String spop(String key);

	Set<String> spop(String key, int num);

	long sadd(String key, String value);

	long zadd(String key, Double score, String value);

	Long scard(String key);

	Long geoadd(String key, double longitude, double latitude, String member);

	Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap);

	String brpoplpushdemand(String key);

	Long decr(String key);

	void watch(String key);

	Transaction multi();

	void unwatch(String key);

	long delPostCountifZero(String posCountKey);

//	long delZanInfo(String userInfoKey, String posCountKey, int addtag);

	long zrem(String key, String value);

	Set<Tuple> ZREVRANGEBYSCORE(String key, double max, double min, int size);

	Set<String> zrange(String key);

//	long delZanInfo(String userInfoKey, String posCountKey, String userLikeSetKey, String userUnLinkKey, String postId,
//			int addtag);

	long delZanInfo(String userInfoKey, String posCountKey, int addtag, String status);

	Long zrank(String key, String member);

	long delZanInfo(String userInfoKey, String posCountKey, String userLikeSetKey, String userUnLinkKey, String postId,
			int addtag, String status);

	long setnx(String key, String value);

	String poll(String queueName);

	Long incrBy(String key, long num);

	long delZanInfoForScore(String userInfoKey, String posCountKey, int addtag, String status, Long score);

	Set<String> zrangeMinMax(String key, int direction);

	Double zscore(String key, String member);

	Long zcard(String key);

	String ltrim(String key, String value);

	Set<Tuple> zrangeWithScore(String key, int start, int end);

	void ltrim(String key, int begin, int end);

	Long llen(String key);

	String lindex(String listKey, int i);

}

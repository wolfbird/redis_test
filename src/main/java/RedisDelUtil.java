import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisDelUtil {
	
	public  static  JedisPool jedisPool;

	private static  final Logger log = Logger.getLogger(RedisDelUtil.class);

	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("redis");
		int maxActive = Integer.parseInt(resourceBundle.getString("redis.pool.maxActive"));
		int maxIdle = Integer.parseInt(resourceBundle.getString("redis.pool.maxIdle"));
		int maxWait = Integer.parseInt(resourceBundle.getString("redis.pool.maxWait"));
		
		String ip = resourceBundle.getString("redis.ip");
		int port = Integer.parseInt(resourceBundle.getString("redis.port"));
		
		JedisPoolConfig config = new JedisPoolConfig();  
		//最大连接数
		config.setMaxTotal(maxActive);
		//最大空闲数
		config.setMaxIdle(maxIdle);
		//超时时间
		config.setMaxWaitMillis(maxWait);
		
		jedisPool = new JedisPool(config, ip, port);
	}


	public static boolean del(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if(jedis == null|| !jedis.exists(key)){
				log.info("key:"+key+" is not found");
				if (jedis != null) {
					jedis.close();
				}
				return false;
			}
			jedis.del(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if (jedis != null) {
				jedis.close();
			}
		}
	}


	public static Object get(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if(jedis == null|| !jedis.exists(key)){
				log.info("key:"+key+" is not found");
				if (jedis != null) {
					jedis.close();
				}
				return null;
			}
			Object value = jedis.get(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	


}

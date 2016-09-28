import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.redis.RedisUtil;
import com.alibaba.druid.pool.DruidDataSource;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by xiaofengxu on 16/9/27.
 */
public class InitResource  {

    public InitResource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("superid");
        dataSource.setPassword("superid");
        dataSource.setUrl("jdbc:mysql://rm-bp1943x791y4e3z21.mysql.rds.aliyuncs.com/jpa");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(1000);
        new JdbcSessionFactory(dataSource);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(300);
        jedisPoolConfig.setTestOnBorrow(true);
        new RedisUtil(jedisPoolConfig);
    }
}

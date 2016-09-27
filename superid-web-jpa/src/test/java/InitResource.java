import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.redis.RedisUtil;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by xiaofengxu on 16/9/27.
 */
public class InitResource extends BlockJUnit4ClassRunner {
    public static DruidDataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("superid");
        dataSource.setPassword("superid");
        dataSource.setUrl("jdbc:mysql://rm-bp1943x791y4e3z21.mysql.rds.aliyuncs.com/jpa");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(1000);
        return dataSource;
    }

    static {
        new JdbcSessionFactory(getDataSource());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(300);
        jedisPoolConfig.setTestOnBorrow(true);
        new RedisUtil(jedisPoolConfig);
    }

    public InitResource(Class<?> klass) throws InitializationError {
        super(klass);
    }

}

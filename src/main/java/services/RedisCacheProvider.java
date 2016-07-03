package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.vo.getmarvelcharacters.GetMarvelCharacters;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.UnknownHostException;

/**
 * Created by PabloGallazzi on 2/7/16.
 */
@Service
@Scope("singleton")
@Profile({"openshift"})
public class RedisCacheProvider implements CacheProvider {

    JedisPool pool;

    @PostConstruct
    public void initialize() throws UnknownHostException {
        String host = System.getenv("OPENSHIFT_REDIS_HOST");
        String port = System.getenv("OPENSHIFT_REDIS_PORT");
        Integer port_number = new Integer(port);
        String pass = System.getenv("REDIS_PASSWORD");
        pool = new JedisPool(new JedisPoolConfig(), host, port_number, 2000, pass, 0);
    }

    @PreDestroy
    public void tearDown() {
        pool.destroy();
    }

    @Override
    public void saveCharactersToCache(String key, GetMarvelCharacters characters) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Gson gson = new GsonBuilder().create();
            jedis.setex(key, 21600, gson.toJson(characters));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public GetMarvelCharacters getCharactersFromCache(String key) {
        Jedis jedis = null;
        GetMarvelCharacters chara;
        try {
            jedis = pool.getResource();
            Gson gson = new GsonBuilder().create();
            chara = gson.fromJson(jedis.get(key), GetMarvelCharacters.class);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return chara;
    }
}

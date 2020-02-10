package zy.pointer.j2easy.startup.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * 配置 Spring-boot Cache 模块中的缓存实现
 */
@Configuration
@EnableCaching
public class CacheManagerConfig {

    @Autowired
    RedisTemplate redisTemplate;

    @Bean public CacheManager cacheManager(){
        //缓存配置对象
         RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
         redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L))                                                    //设置缓存的默认超时时间：30分钟
                 .disableCachingNullValues()                                                                                                    //如果是空值，不缓存
                 .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer( redisTemplate.getStringSerializer() ))          //设置key序列化器
                 .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(( redisTemplate.getHashValueSerializer() )));  //设置value序列化器
         return RedisCacheManager
                 .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory()))
                 .cacheDefaults(redisCacheConfiguration).build();
    }

}

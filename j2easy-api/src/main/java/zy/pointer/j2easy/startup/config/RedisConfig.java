package zy.pointer.j2easy.startup.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置 Redis 序列化相关
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        /*
         * 配置 序列化器
         *
         * String -> StringRedisSerializer
         * Object -> GenericJackson2JsonRedisSerializer
         */
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//      Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        /**
         *  Why GenericJackson2JsonRedisSerializer Not Jackson2JsonRedisSerializer ?
         *  序列化的JSON中包含了 Class 信息,这样以便于 Springboot Cache 框架自动缓存反序列化
         */
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        return template;
    }

}

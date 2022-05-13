package com.myserver.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30L))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                ;
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("emailCode");
        cacheNames.add("usernameNum");
        cacheNames.add("emailNum");
        cacheNames.add("distributor");
        ConcurrentHashMap<String, RedisCacheConfiguration> configMap = new ConcurrentHashMap<>();
        configMap.put("emailCode", config.entryTtl(Duration.ofMinutes(6L)));
        configMap.put("usernameNum", config.entryTtl(Duration.ofMinutes(30L)));
        configMap.put("emailNum", config.entryTtl(Duration.ofMinutes(30L)));
        configMap.put("distributor", config);
        //这个需要改

        //需要先初始化缓存名称，再初始化其它的配置。
        return RedisCacheManager.builder(factory).cacheDefaults(config).initialCacheNames(cacheNames).withInitialCacheConfigurations(configMap).build();
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        template.setConnectionFactory(factory);
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(jackson2JsonRedisSerializer);//如果key是String 需要配置一下StringSerializer,不然key会乱码 /XX/XX
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}

package com.atyan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 用于配置RedisTemplate，以定义Redis操作的序列化方式
 */
@Configuration
public class RedisConfig {

    /**
     * 配置RedisTemplate的Bean
     * 此方法定义了Redis连接工厂和数据序列化方式，以确保Redis操作的一致性和高效性
     *
     * @param connectionFactory Redis连接工厂，用于创建和管理Redis连接
     * @return 配置好的RedisTemplate实例，用于执行Redis操作
     */
    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        // 这是因为字符串是最常用的key类型，使用专门的序列化器可以提高效率和可读性
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        // 这保持了与普通key相同的序列化策略，简化了使用和理解
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        // 初始化RedisTemplate，确保所有配置生效
        template.afterPropertiesSet();
        return template;
    }
}

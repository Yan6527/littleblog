package com.atyan.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.util.Assert;
import java.nio.charset.Charset;

/**
 * 使用FastJson实现的Redis序列化器
 * @param <T> 序列化和反序列化的类型
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>{

    // 默认字符集
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    // 序列化和反序列化的类类型
    private Class<T> clazz;

    // 静态代码块，设置全局解析配置
    static
    {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    /**
     * 构造函数
     * @param clazz 序列化和反序列化的类类型
     */
    public FastJsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化方法
     * 将对象转换为byte数组
     * @param t 要序列化的对象
     * @return 序列化后的byte数组
     * @throws SerializationException 如果序列化失败
     */
    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null)
        {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化方法
     * 将byte数组转换回对象
     * @param bytes 序列化后的byte数组
     * @return 反序列化后的对象
     * @throws SerializationException 如果反序列化失败
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0)
        {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }

    /**
     * 获取Java类型
     * @param clazz 类型类
     * @return JavaType对象
     */
    protected JavaType getJavaType(Class<?> clazz)
    {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}

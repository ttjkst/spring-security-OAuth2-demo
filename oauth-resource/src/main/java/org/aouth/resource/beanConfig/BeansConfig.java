package org.aouth.resource.beanConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class BeansConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


}

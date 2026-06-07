package kr.magicbox.media.adapter.in.scheduler;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

    @Value("${redisson.host}")
    private String host;

    @Value("${redisson.port}")
    private int port;

    @Value("${redisson.password:}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password.isEmpty() ? null : password);
        return Redisson.create(config);
    }
}

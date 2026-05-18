package kr.magicbox.delivery.global.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "kr.magicbox.delivery.adapter.out.communication")
public class FeignClientConfiguration {
}

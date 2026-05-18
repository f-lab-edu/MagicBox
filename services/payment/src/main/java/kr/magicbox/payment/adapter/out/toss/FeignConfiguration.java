package kr.magicbox.payment.adapter.out.toss;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "kr.magicbox.payment.adapter.out.toss")
public class FeignConfiguration {
}

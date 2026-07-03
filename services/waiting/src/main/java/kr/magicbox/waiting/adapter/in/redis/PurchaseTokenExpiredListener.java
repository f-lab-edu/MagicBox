package kr.magicbox.waiting.adapter.in.redis;

import kr.magicbox.waiting.application.port.out.AdmissionQueuePort;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.global.properties.WaitingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PurchaseTokenExpiredListener extends KeyExpirationEventMessageListener {

    private final AdmissionQueuePort admissionQueuePort;
    private final WaitingProperties waitingProperties;

    public PurchaseTokenExpiredListener(
            RedisMessageListenerContainer listenerContainer,
            AdmissionQueuePort admissionQueuePort,
            WaitingProperties waitingProperties) {
        super(listenerContainer);
        this.admissionQueuePort = admissionQueuePort;
        this.waitingProperties = waitingProperties;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        String tokenKeyPrefix = waitingProperties.getTokenKeyPrefix();

        if (!expiredKey.startsWith(tokenKeyPrefix)) {
            return;
        }

        // key: purchase_token:{releaseId}:{userId}
        String withoutPrefix = expiredKey.substring(tokenKeyPrefix.length());
        int colonIdx = withoutPrefix.indexOf(':');
        if (colonIdx < 0) {
            log.warn("[TTL-EXPIRE] 파싱 실패 key={}", expiredKey);
            return;
        }

        String releaseIdValue = withoutPrefix.substring(0, colonIdx);
        ReleaseId releaseId = ReleaseId.of(Long.parseLong(releaseIdValue));

        log.info("[TTL-EXPIRE] purchase_token 만료 → deactivate releaseId={} key={}", releaseIdValue, expiredKey);
        admissionQueuePort.deactivate(releaseId)
                .doOnError(e -> log.warn("[TTL-EXPIRE] deactivate 실패 releaseId={}", releaseIdValue, e))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }
}

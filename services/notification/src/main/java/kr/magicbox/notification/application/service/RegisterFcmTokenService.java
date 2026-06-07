package kr.magicbox.notification.application.service;

import kr.magicbox.notification.adapter.out.persistence.entity.FcmTokenEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.FcmTokenJpaRepository;
import kr.magicbox.notification.application.dto.command.RegisterFcmTokenCommand;
import kr.magicbox.notification.application.port.in.RegisterFcmTokenUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterFcmTokenService implements RegisterFcmTokenUseCase {

    private final FcmTokenJpaRepository fcmTokenJpaRepository;

    @Override
    @Transactional
    public void register(RegisterFcmTokenCommand command) {
        if (fcmTokenJpaRepository.existsByUserIdAndToken(command.userId(), command.token())) {
            return;
        }
        fcmTokenJpaRepository.save(FcmTokenEntity.builder()
                .userId(command.userId())
                .token(command.token())
                .build());
        log.info("[FCM] 토큰 등록 완료. userId={}", command.userId());
    }
}

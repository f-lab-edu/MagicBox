package kr.magicbox.notification.adapter.in.web.controller;

import kr.magicbox.notification.adapter.in.web.dto.response.NotificationResponse;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationTemplateEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationTemplateJpaRepository;
import kr.magicbox.notification.application.port.out.NotificationRepositoryPort;
import kr.magicbox.notification.domain.aggregate.Notification;
import kr.magicbox.notification.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationRepositoryPort notificationRepositoryPort;
    private final NotificationTemplateJpaRepository notificationTemplateJpaRepository;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(@AuthenticationPrincipal UserId userId) {
        List<Notification> notifications = notificationRepositoryPort.findAllByUserId(userId.value());

        Map<kr.magicbox.notification.domain.enums.NotificationType, NotificationTemplateEntity> templateMap =
                notificationTemplateJpaRepository.findAll().stream()
                        .collect(Collectors.toMap(NotificationTemplateEntity::getType, t -> t));

        List<NotificationResponse> responses = notifications.stream()
                .filter(n -> templateMap.containsKey(n.getType()))
                .map(n -> NotificationResponse.of(n, templateMap.get(n.getType())))
                .toList();

        return ResponseEntity.ok(responses);
    }
}

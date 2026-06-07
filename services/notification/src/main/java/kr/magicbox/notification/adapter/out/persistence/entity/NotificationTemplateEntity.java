package kr.magicbox.notification.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.notification.domain.enums.NotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_template")
public class NotificationTemplateEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Builder
    public NotificationTemplateEntity(NotificationType type, String title, String body) {
        this.type = type;
        this.title = title;
        this.body = body;
    }
}

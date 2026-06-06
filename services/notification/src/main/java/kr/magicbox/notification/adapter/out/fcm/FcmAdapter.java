package kr.magicbox.notification.adapter.out.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.magicbox.notification.application.port.out.FcmTopicPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmAdapter implements FcmTopicPort {

    @Override
    public void sendToTopic(String topic, String title, String body) {
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String messageId = FirebaseMessaging.getInstance().send(message);
            log.info("[FCM] Topic 메시지 전송 완료. topic={}, messageId={}", topic, messageId);
        } catch (FirebaseMessagingException e) {
            log.error("[FCM] Topic 메시지 전송 실패. topic={}, error={}", topic, e.getMessage(), e);
            throw new RuntimeException("FCM 메시지 전송 실패: " + topic, e);
        }
    }
}

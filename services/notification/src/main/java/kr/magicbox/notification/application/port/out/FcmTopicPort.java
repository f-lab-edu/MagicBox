package kr.magicbox.notification.application.port.out;

public interface FcmTopicPort {
    void sendToTopic(String topic, String title, String body);
}

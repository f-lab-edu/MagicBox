package kr.magicbox.media.adapter.in.kafka;

import kr.magicbox.media.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.media.adapter.in.kafka.event.CreatorProfileUpdatedEvent;
import kr.magicbox.media.adapter.in.kafka.event.GeneralGoodsCreatedEvent;
import kr.magicbox.media.adapter.in.kafka.event.GeneralGoodsDeletedEvent;
import kr.magicbox.media.adapter.in.kafka.event.GeneralGoodsUpdatedEvent;
import kr.magicbox.media.adapter.in.kafka.event.ReleaseCreatedEvent;
import kr.magicbox.media.adapter.in.kafka.event.ReleaseDeletedEvent;
import kr.magicbox.media.adapter.in.kafka.event.ReleaseUpdatedEvent;
import kr.magicbox.media.adapter.in.kafka.event.UserProfileUpdatedEvent;
import kr.magicbox.media.application.port.in.ActivateMediaUseCase;
import kr.magicbox.media.application.port.in.DeleteMediaByUuidsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaEventKafkaListener {

    private final ActivateMediaUseCase activateMediaUseCase;
    private final DeleteMediaByUuidsUseCase deleteMediaByUuidsUseCase;

    @Idempotent
    @KafkaListener(topics = "outbox.event.release-created", groupId = "media-service")
    public void onReleaseCreated(ConsumerRecord<String, ReleaseCreatedEvent> record) {
        ReleaseCreatedEvent event = record.value();
        if (event.mediaUrls() == null || event.mediaUrls().isEmpty()) return;
        log.debug("[Media] release-created 수신. releaseId={}", event.releaseId());
        activateMediaUseCase.activateByUuids(event.mediaUrls());
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.release-updated", groupId = "media-service")
    public void onReleaseUpdated(ConsumerRecord<String, ReleaseUpdatedEvent> record) {
        ReleaseUpdatedEvent event = record.value();
        log.debug("[Media] release-updated 수신. releaseId={}", event.releaseId());

        List<String> beforeUrls = nullSafe(event.before().mediaUrls());
        List<String> afterUrls = nullSafe(event.after().mediaUrls());

        List<String> toActivate = afterUrls.stream().filter(url -> !beforeUrls.contains(url)).toList();
        List<String> toDelete = beforeUrls.stream().filter(url -> !afterUrls.contains(url)).toList();

        if (!toActivate.isEmpty()) activateMediaUseCase.activateByUuids(toActivate);
        if (!toDelete.isEmpty()) deleteMediaByUuidsUseCase.deleteByUuids(toDelete);
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.release-deleted", groupId = "media-service")
    public void onReleaseDeleted(ConsumerRecord<String, ReleaseDeletedEvent> record) {
        ReleaseDeletedEvent event = record.value();
        if (event.mediaUrls() == null || event.mediaUrls().isEmpty()) return;
        log.debug("[Media] release-deleted 수신. releaseId={}", event.releaseId());
        deleteMediaByUuidsUseCase.deleteByUuids(event.mediaUrls());
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.general-goods-created", groupId = "media-service")
    public void onGeneralGoodsCreated(ConsumerRecord<String, GeneralGoodsCreatedEvent> record) {
        GeneralGoodsCreatedEvent event = record.value();
        if (event.mediaUrls() == null || event.mediaUrls().isEmpty()) return;
        log.debug("[Media] general-goods-created 수신. generalGoodsId={}", event.generalGoodsId());
        activateMediaUseCase.activateByUuids(event.mediaUrls());
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.general-goods-updated", groupId = "media-service")
    public void onGeneralGoodsUpdated(ConsumerRecord<String, GeneralGoodsUpdatedEvent> record) {
        GeneralGoodsUpdatedEvent event = record.value();
        log.debug("[Media] general-goods-updated 수신. generalGoodsId={}", event.generalGoodsId());

        List<String> beforeUrls = nullSafe(event.before().mediaUrls());
        List<String> afterUrls = nullSafe(event.after().mediaUrls());

        List<String> toActivate = afterUrls.stream().filter(url -> !beforeUrls.contains(url)).toList();
        List<String> toDelete = beforeUrls.stream().filter(url -> !afterUrls.contains(url)).toList();

        if (!toActivate.isEmpty()) activateMediaUseCase.activateByUuids(toActivate);
        if (!toDelete.isEmpty()) deleteMediaByUuidsUseCase.deleteByUuids(toDelete);
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.general-goods-deleted", groupId = "media-service")
    public void onGeneralGoodsDeleted(ConsumerRecord<String, GeneralGoodsDeletedEvent> record) {
        GeneralGoodsDeletedEvent event = record.value();
        if (event.mediaUrls() == null || event.mediaUrls().isEmpty()) return;
        log.debug("[Media] general-goods-deleted 수신. generalGoodsId={}", event.generalGoodsId());
        deleteMediaByUuidsUseCase.deleteByUuids(event.mediaUrls());
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.creator-profile-updated", groupId = "media-service")
    public void onCreatorProfileUpdated(ConsumerRecord<String, CreatorProfileUpdatedEvent> record) {
        CreatorProfileUpdatedEvent event = record.value();
        log.debug("[Media] creator-profile-updated 수신. creatorId={}", event.creatorId());

        String beforeUrl = event.before().profileImageUrl();
        String afterUrl = event.after().profileImageUrl();

        if (afterUrl != null && !afterUrl.equals(beforeUrl)) {
            activateMediaUseCase.activateByUuids(List.of(afterUrl));
        }
        if (beforeUrl != null && !beforeUrl.equals(afterUrl)) {
            deleteMediaByUuidsUseCase.deleteByUuids(List.of(beforeUrl));
        }
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.user-profile-updated", groupId = "media-service")
    public void onUserProfileUpdated(ConsumerRecord<String, UserProfileUpdatedEvent> record) {
        UserProfileUpdatedEvent event = record.value();
        log.debug("[Media] user-profile-updated 수신. userId={}", event.userId());

        String beforeUrl = event.before().profileImageUrl();
        String afterUrl = event.after().profileImageUrl();

        if (afterUrl != null && !afterUrl.equals(beforeUrl)) {
            activateMediaUseCase.activateByUuids(List.of(afterUrl));
        }
        if (beforeUrl != null && !beforeUrl.equals(afterUrl)) {
            deleteMediaByUuidsUseCase.deleteByUuids(List.of(beforeUrl));
        }
    }

    private List<String> nullSafe(List<String> list) {
        return list != null ? list : new ArrayList<>();
    }
}

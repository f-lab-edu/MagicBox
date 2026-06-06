package kr.magicbox.media.adapter.out.persistence.repository;

import kr.magicbox.media.adapter.out.persistence.entity.MediaEntity;
import kr.magicbox.media.domain.enums.MediaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MediaJpaRepository extends JpaRepository<MediaEntity, Long> {
    Optional<MediaEntity> findByUuid(String uuid);
    List<MediaEntity> findAllByUuidIn(List<String> uuids);
    List<MediaEntity> findAllByStatusAndCreatedAtBefore(MediaStatus status, Instant threshold);
    void deleteByUuid(String uuid);
    void deleteAllByUuidIn(List<String> uuids);
}

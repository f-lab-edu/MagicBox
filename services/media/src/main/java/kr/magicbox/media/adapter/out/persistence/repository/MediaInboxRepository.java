package kr.magicbox.media.adapter.out.persistence.repository;

import kr.magicbox.media.adapter.out.persistence.entity.MediaInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaInboxRepository extends JpaRepository<MediaInboxEntity, Long> {
    boolean existsByKey(String key);
}

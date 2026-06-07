package kr.magicbox.media.application.port.out;

import kr.magicbox.media.domain.aggregate.Media;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MediaRepositoryPort {
    Media save(Media media);
    Optional<Media> findByUuid(String uuid);
    List<Media> findAllByUuids(List<String> uuids);
    List<Media> findInactiveOlderThan(Instant threshold);
    void update(Media media);
    void deleteByUuid(String uuid);
    void deleteAllByUuids(List<String> uuids);
}

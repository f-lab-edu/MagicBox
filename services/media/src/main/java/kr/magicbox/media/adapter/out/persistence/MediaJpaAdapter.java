package kr.magicbox.media.adapter.out.persistence;

import kr.magicbox.media.adapter.out.persistence.entity.MediaEntity;
import kr.magicbox.media.adapter.out.persistence.repository.MediaJpaRepository;
import kr.magicbox.media.application.port.out.MediaRepositoryPort;
import kr.magicbox.media.domain.aggregate.Media;
import kr.magicbox.media.domain.enums.MediaStatus;
import kr.magicbox.media.domain.exception.MediaNotFoundException;
import kr.magicbox.media.domain.vo.MediaId;
import kr.magicbox.media.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MediaJpaAdapter implements MediaRepositoryPort {

    private final MediaJpaRepository mediaJpaRepository;

    @Override
    public Media save(Media media) {
        MediaEntity entity = MediaEntity.builder()
                .uploaderId(media.getUploaderId().value())
                .uuid(media.getUuid())
                .fileName(media.getFileName())
                .contentType(media.getContentType())
                .status(media.getStatus())
                .build();
        MediaEntity saved = mediaJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Media> findByUuid(String uuid) {
        return mediaJpaRepository.findByUuid(uuid).map(this::toDomain);
    }

    @Override
    public List<Media> findAllByUuids(List<String> uuids) {
        return mediaJpaRepository.findAllByUuidIn(uuids).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Media> findInactiveOlderThan(Instant threshold) {
        return mediaJpaRepository.findAllByStatusAndCreatedAtBefore(MediaStatus.INACTIVE, threshold).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void update(Media media) {
        MediaEntity entity = mediaJpaRepository.findByUuid(media.getUuid())
                .orElseThrow(MediaNotFoundException::new);
        if (media.getStatus() == MediaStatus.ACTIVE) {
            entity.activate();
        }
    }

    @Override
    public void deleteByUuid(String uuid) {
        mediaJpaRepository.deleteByUuid(uuid);
    }

    @Override
    public void deleteAllByUuids(List<String> uuids) {
        mediaJpaRepository.deleteAllByUuidIn(uuids);
    }

    private Media toDomain(MediaEntity entity) {
        return Media.builder()
                .id(MediaId.of(entity.getId()))
                .uploaderId(UserId.of(entity.getUploaderId()))
                .uuid(entity.getUuid())
                .fileName(entity.getFileName())
                .contentType(entity.getContentType())
                .status(entity.getStatus())
                .build();
    }
}

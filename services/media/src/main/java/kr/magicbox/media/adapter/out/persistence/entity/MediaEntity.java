package kr.magicbox.media.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kr.magicbox.media.domain.enums.MediaStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "media")
public class MediaEntity extends BaseEntity {

    @Column(nullable = false)
    private Long uploaderId;

    @Column(nullable = false, unique = true, length = 36)
    private String uuid;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaStatus status;

    @Builder
    public MediaEntity(Long uploaderId, String uuid, String fileName, String contentType,
                       MediaStatus status) {
        this.uploaderId = uploaderId;
        this.uuid = uuid;
        this.fileName = fileName;
        this.contentType = contentType;
        this.status = status;
    }

    public void activate() {
        this.status = MediaStatus.ACTIVE;
    }
}

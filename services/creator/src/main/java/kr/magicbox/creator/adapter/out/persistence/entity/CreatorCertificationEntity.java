package kr.magicbox.creator.adapter.out.persistence.entity;

import com.github.lian2945.sonyflake.annotation.SonyflakeId;
import jakarta.persistence.*;
import kr.magicbox.creator.adapter.out.persistence.vo.CreatorCertificationRequestVO;
import kr.magicbox.creator.adapter.out.persistence.vo.CreatorCertificationResultVO;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "creator_certification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class CreatorCertificationEntity {

    @Id
    @SonyflakeId
    private Long id;

    @Version
    private Long version;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreatorCertificationStatus status;

    @Embedded
    private CreatorCertificationRequestVO request;

    @Embedded
    private CreatorCertificationResultVO result;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Builder
    public CreatorCertificationEntity(Long userId, CreatorCertificationRequestVO request) {
        this.userId = userId;
        this.status = CreatorCertificationStatus.PENDING;
        this.request = request;
    }

    public void updateFromDomain(CreatorCertification creatorCertification) {
        this.status = creatorCertification.getStatus();
        this.request = CreatorCertificationRequestVO.of(creatorCertification.getRequest());
        this.result = CreatorCertificationResultVO.of(creatorCertification.getResult());
    }
}

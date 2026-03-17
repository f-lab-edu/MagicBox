package kr.magicbox.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.user.domain.enums.DeviceType;
import kr.magicbox.user.adapter.exception.EntityValidationException;
import kr.magicbox.user.domain.aggregate.Device;
import kr.magicbox.user.global.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "device")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeviceEntity extends BaseEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private String deviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private DeviceType deviceType;

    @Column(nullable = false)
    private String version;

    @Column
    private Instant lastActiveAt;

    @Column(nullable = false)
    private Boolean isActive;

    @Builder
    public DeviceEntity(String deviceId, DeviceType deviceType, String version) {
        validateFields(deviceId, deviceType, version);

        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.version = version;
        this.isActive = true;
        this.lastActiveAt = Instant.now();
    }

    private void validateFields(String deviceId, DeviceType deviceType, String appVersion) {
        // 필수 필드 검증
        
        if (deviceId == null || deviceId.isEmpty()) {
            throw new EntityValidationException("디바이스 ID는 필수 값입니다.");
        }
        
        if (deviceType == null) {
            throw new EntityValidationException("디바이스 타입은 필수 값입니다.");
        }

        if (appVersion == null || appVersion.isEmpty()) {
            throw new EntityValidationException("앱 버전은 필수 값입니다.");
        }
    }

    public void updateFromDomain(Device device) {
        this.version = device.getVersion();
        this.lastActiveAt = device.getLastActiveAt();
        this.isActive = device.isActive();
    }

}
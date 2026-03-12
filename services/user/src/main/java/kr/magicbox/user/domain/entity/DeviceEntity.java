package kr.magicbox.user.domain.entity;

import jakarta.persistence.*;
import kr.magicbox.user.domain.enums.DeviceType;
import kr.magicbox.user.domain.exception.InvalidFieldException;
import kr.magicbox.user.global.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "device")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
            throw new InvalidFieldException("디바이스 ID는 필수 값입니다.");
        }
        
        if (deviceType == null) {
            throw new InvalidFieldException("디바이스 타입은 필수 값입니다.");
        }

        if (appVersion == null || appVersion.isEmpty()) {
            throw new InvalidFieldException("앱 버전은 필수 값입니다.");
        }
    }

    public void updateVersion(String version) {
        if (version == null || version.isEmpty()) {
            throw new InvalidFieldException("앱 버전은 필수 값입니다.");
        }
        this.version = version;
    }

    public void updateLastActiveTime() {
        this.lastActiveAt = Instant.now();
    }

    public void activate() {
        this.isActive = true;
        this.lastActiveAt = Instant.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

}
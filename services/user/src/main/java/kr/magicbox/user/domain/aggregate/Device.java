package kr.magicbox.user.domain.aggregate;

import kr.magicbox.user.domain.vo.DeviceId;
import kr.magicbox.user.domain.enums.DeviceType;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Device {
    private final DeviceId id;
    private final String deviceId;
    private final DeviceType deviceType;
    private String version;
    private Instant lastActiveAt;
    private Boolean isActive;

    @Builder
    public Device(Long id, String deviceId, DeviceType deviceType, String version) {
        this.id = DeviceId.of(id);
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.version = version;
        this.isActive = true;
        this.lastActiveAt = Instant.now();
    }

    public void updateVersion(String newVersion) {
        if (newVersion != null && !newVersion.trim().isEmpty()) {
            this.version = newVersion;
        }
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

    public boolean isActive() {
        return Boolean.TRUE.equals(this.isActive);
    }
}

package kr.magicbox.user.domain.aggregate;

import kr.magicbox.user.domain.vo.UserId;
import kr.magicbox.user.domain.vo.DeviceId;
import lombok.Builder;
import lombok.Getter;


@Getter
public class UserDevice {
    private final Long id;
    private final UserId userId;
    private final DeviceId deviceId;
    private Boolean isActive;

    @Builder
    public UserDevice(Long id, UserId userId, DeviceId deviceId, Boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.deviceId = deviceId;
        this.isActive = isActive != null ? isActive : true;
    }

    public void connect() {
        this.isActive = true;
    }

    public void disconnect() {
        this.isActive = false;
    }

    public boolean isConnected() {
        return Boolean.TRUE.equals(this.isActive);
    }
}

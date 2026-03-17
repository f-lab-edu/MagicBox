package kr.magicbox.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.user.adapter.exception.EntityValidationException;
import kr.magicbox.user.domain.aggregate.UserDevice;
import kr.magicbox.user.global.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "user_device",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "device_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDeviceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private DeviceEntity device;

    @Column(nullable = false)
    private Boolean isActive;

    @Builder
    public UserDeviceEntity(UserEntity user, DeviceEntity device) {
        validateFields(user, device);
        
        this.user = user;
        this.device = device;
        this.isActive = true;
    }

    private void validateFields(UserEntity user, DeviceEntity device) {
        if (user == null) {
            throw new EntityValidationException("사용자는 필수 값입니다.");
        }
        
        if (device == null) {
            throw new EntityValidationException("디바이스는 필수 값입니다.");
        }
    }

    public void updateFromDomain(UserDevice userDevice) {
        this.isActive = userDevice.isConnected();
    }
}
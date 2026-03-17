package kr.magicbox.user.adapter.out.persistence.mapper;

import kr.magicbox.user.domain.aggregate.UserDevice;
import kr.magicbox.user.adapter.out.persistence.entity.UserDeviceEntity;
import kr.magicbox.user.adapter.out.persistence.entity.UserEntity;
import kr.magicbox.user.adapter.out.persistence.entity.DeviceEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDeviceMapper {

    public UserDeviceEntity toEntity(UserDevice userDevice, UserEntity userEntity, DeviceEntity deviceEntity) {
        UserDeviceEntity entity = UserDeviceEntity.builder()
                .user(userEntity)
                .device(deviceEntity)
                .build();
        
        if (!userDevice.isConnected()) {
            entity.updateFromDomain(userDevice);
        }
        
        return entity;
    }

    public UserDevice toDomain(UserDeviceEntity entity) {
        UserDevice userDevice = UserDevice.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .deviceId(entity.getDevice().getId())
                .build();
        
        if (!entity.getIsActive()) {
            userDevice.disconnect();
        }
        
        return userDevice;
    }

    public void updateEntity(UserDevice userDevice, UserDeviceEntity entity) {
        entity.updateFromDomain(userDevice);
    }
}
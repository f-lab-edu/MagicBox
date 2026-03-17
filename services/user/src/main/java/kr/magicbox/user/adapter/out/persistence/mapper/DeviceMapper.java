package kr.magicbox.user.adapter.out.persistence.mapper;

import kr.magicbox.user.domain.aggregate.Device;
import kr.magicbox.user.adapter.out.persistence.entity.DeviceEntity;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceEntity toEntity(Device device) {
        return DeviceEntity.builder()
                .deviceId(device.getDeviceId())
                .deviceType(device.getDeviceType())
                .version(device.getVersion())
                .build();
    }

    public Device toDomain(DeviceEntity entity) {
        return Device.builder()
                .id(entity.getId())
                .deviceId(entity.getDeviceId())
                .deviceType(entity.getDeviceType())
                .version(entity.getVersion())
                .build();
    }

    public void updateEntity(Device device, DeviceEntity entity) {
        entity.updateFromDomain(device);
    }
}
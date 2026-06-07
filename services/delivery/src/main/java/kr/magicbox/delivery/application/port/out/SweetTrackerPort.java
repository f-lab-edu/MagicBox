package kr.magicbox.delivery.application.port.out;

import kr.magicbox.delivery.application.dto.result.SweetTrackerTrackingInfo;

public interface SweetTrackerPort {
    SweetTrackerTrackingInfo getTrackingInfo(String carrierCode, String trackingNumber);
}

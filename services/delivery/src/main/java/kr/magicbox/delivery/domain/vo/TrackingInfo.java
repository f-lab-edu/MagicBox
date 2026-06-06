package kr.magicbox.delivery.domain.vo;

import kr.magicbox.delivery.domain.exception.InvalidFieldException;

public record TrackingInfo(
        String carrierCode,
        String trackingNumber
) {

    public TrackingInfo {
        if (carrierCode == null || carrierCode.isBlank()) {
            throw new InvalidFieldException("택배사 코드는 필수 값입니다.");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new InvalidFieldException("운송장 번호는 필수 값입니다.");
        }
    }

    public static TrackingInfo of(String carrierCode, String trackingNumber) {
        return new TrackingInfo(carrierCode, trackingNumber);
    }
}

package kr.magicbox.delivery.adapter.out.communication.sweettracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SweetTrackerTrackingInfoResponse(
        Boolean complete,
        String invoiceNo,
        Integer level,
        String result,
        @JsonProperty("trackingDetails") List<TrackingDetail> trackingDetails
) {
    public record TrackingDetail(
            String timeString,
            String where,
            String kind,
            Integer level
    ) {}
}

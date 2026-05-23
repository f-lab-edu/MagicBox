package kr.magicbox.delivery.application.dto.result;

import java.util.List;

public record SweetTrackerTrackingInfo(
        boolean complete,
        Integer level,
        List<TrackingDetail> details
) {
    public record TrackingDetail(
            String time,
            String where,
            String kind,
            Integer level
    ) {}
}

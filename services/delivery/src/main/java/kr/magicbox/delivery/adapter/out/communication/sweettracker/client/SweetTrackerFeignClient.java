package kr.magicbox.delivery.adapter.out.communication.sweettracker.client;

import kr.magicbox.delivery.adapter.out.communication.sweettracker.dto.SweetTrackerTrackingInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sweetTracker", url = "${sweet-tracker.base-url}", configuration = SweetTrackerFeignErrorDecoder.class)
public interface SweetTrackerFeignClient {

    @GetMapping("/api/v1/trackingInfo")
    SweetTrackerTrackingInfoResponse getTrackingInfo(
            @RequestParam("t_key") String tKey,
            @RequestParam("t_code") String tCode,
            @RequestParam("t_invoice") String tInvoice
    );
}

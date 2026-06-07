package kr.magicbox.delivery.adapter.out.communication.sweettracker.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.exception.SweetTrackerBadRequestException;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.exception.SweetTrackerServiceUnavailableException;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.exception.SweetTrackerTimeoutException;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.exception.SweetTrackerUnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SweetTrackerFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        if (status == HttpStatus.UNAUTHORIZED.value()) {
            return new SweetTrackerUnauthorizedException();
        }
        if (status == HttpStatus.REQUEST_TIMEOUT.value() || status == HttpStatus.GATEWAY_TIMEOUT.value()) {
            return new SweetTrackerTimeoutException();
        }
        if (isClientError(status)) {
            return new SweetTrackerBadRequestException();
        }
        return new SweetTrackerServiceUnavailableException(new RuntimeException("스윗트래커 서버 오류. status=" + status));
    }

    private boolean isClientError(int status) {
        return status >= 400 && status < 500;
    }
}

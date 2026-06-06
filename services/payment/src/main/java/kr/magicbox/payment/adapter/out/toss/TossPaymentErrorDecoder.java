package kr.magicbox.payment.adapter.out.toss;

import tools.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import kr.magicbox.payment.adapter.out.toss.dto.TossErrorResponse;
import kr.magicbox.payment.adapter.out.toss.exception.TossPaymentException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class TossPaymentErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream body = response.body().asInputStream()) {
            TossErrorResponse errorResponse = objectMapper.readValue(body, TossErrorResponse.class);
            log.error("[Toss] 결제 API 오류. code={}, message={}", errorResponse.code(), errorResponse.message());
            return new TossPaymentException(errorResponse.code(), errorResponse.message());
        } catch (IOException e) {
            log.error("[Toss] 오류 응답 파싱 실패. status={}", response.status());
            return new TossPaymentException("UNKNOWN", "Toss 결제 API 오류가 발생했습니다. status=" + response.status());
        }
    }
}

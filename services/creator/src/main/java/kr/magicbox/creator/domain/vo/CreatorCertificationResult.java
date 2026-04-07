package kr.magicbox.creator.domain.vo;

import kr.magicbox.creator.domain.exception.InvalidFieldException;
import lombok.Builder;

import java.time.Instant;

public record CreatorCertificationResult(String reviewMessage, Instant reviewedAt) {

    @Builder
    public CreatorCertificationResult {
        if (reviewMessage == null || reviewMessage.trim().isEmpty()) {
            throw new InvalidFieldException("심사 메시지는 필수 값입니다.");
        }
        if (reviewedAt == null) {
            reviewedAt = Instant.now();
        }
    }

    public static CreatorCertificationResult of(String reviewMessage) {
        return new CreatorCertificationResult(reviewMessage, Instant.now());
    }
}
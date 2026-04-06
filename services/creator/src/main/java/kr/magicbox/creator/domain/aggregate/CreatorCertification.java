package kr.magicbox.creator.domain.aggregate;

import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import kr.magicbox.creator.domain.exception.CertificationAlreadyReviewedException;
import kr.magicbox.creator.domain.exception.CertificationNotFoundException;
import kr.magicbox.creator.domain.exception.CertificationPendingAlreadyExistsException;
import kr.magicbox.creator.domain.exception.InvalidCertificationReviewStatusException;
import kr.magicbox.creator.domain.exception.InvalidFieldException;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;
import kr.magicbox.creator.domain.vo.CreatorCertificationRequest;
import kr.magicbox.creator.domain.vo.CreatorCertificationResult;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreatorCertification {

    private final CreatorCertificationId id;
    private final UserId userId;
    private final CreatorCertificationRequest request;
    private CreatorCertificationStatus status;
    private CreatorCertificationResult result;

    public static CreatorCertification create(UserId userId, CreatorCertificationRequest request,
                                              List<CreatorCertification> existingCertifications) {
        validateNoPendingCertification(existingCertifications);
        return CreatorCertification.builder()
                .request(request)
                .userId(userId)
                .status(CreatorCertificationStatus.PENDING)
                .build();
    }

    private static void validateNoPendingCertification(List<CreatorCertification> existingCertifications) {
        boolean hasPending = existingCertifications.stream()
                .anyMatch(c -> c.status == CreatorCertificationStatus.PENDING);
        if (hasPending) {
            throw new CertificationPendingAlreadyExistsException();
        }
    }

    @Builder
    public CreatorCertification(CreatorCertificationId id, UserId userId, CreatorCertificationRequest request, CreatorCertificationStatus status, CreatorCertificationResult result) {
        if (request == null) {
            throw new InvalidFieldException("심사 신청 정보는 필수 값입니다.");
        }
        this.id = id;
        this.userId = userId;
        this.request = request;
        this.status = status != null ? status : CreatorCertificationStatus.PENDING;
        this.result = result;
    }

    public boolean isReviewed() {
        return this.status != CreatorCertificationStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == CreatorCertificationStatus.APPROVED;
    }

    public void review(CreatorCertificationStatus status, CreatorCertificationResult result) {
        if (isReviewed()) {
            throw new CertificationAlreadyReviewedException();
        }
        if (status == CreatorCertificationStatus.PENDING) {
            throw new InvalidCertificationReviewStatusException();
        }
        this.status = status;
        this.result = result;
    }

    public void validateCancellable(UserId requestUserId) {
        if (isReviewed()) {
            throw new CertificationAlreadyReviewedException("이미 심사가 완료된 인증 신청은 취소할 수 없습니다.");
        }
        if (!this.userId.equals(requestUserId)) {
            throw new CertificationNotFoundException();
        }
    }
}

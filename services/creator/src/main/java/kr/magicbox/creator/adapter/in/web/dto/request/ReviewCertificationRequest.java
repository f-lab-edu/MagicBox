package kr.magicbox.creator.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.creator.application.dto.command.ReviewCertificationCommand;
import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;
import lombok.Builder;

@Builder
public record ReviewCertificationRequest(
        @NotNull(message = "인증 상태는 필수입니다.") CreatorCertificationStatus status,
        @NotBlank(message = "심사 메시지는 필수입니다.") String reviewMessage
) {

    public ReviewCertificationCommand toCommand(Long creatorCertificationId) {
        return new ReviewCertificationCommand(
                CreatorCertificationId.of(creatorCertificationId),
                status,
                reviewMessage
        );
    }
}

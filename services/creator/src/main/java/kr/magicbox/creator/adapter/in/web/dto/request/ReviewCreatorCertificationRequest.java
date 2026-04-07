package kr.magicbox.creator.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.creator.application.dto.command.ReviewCreatorCertificationCommand;
import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;
import lombok.Builder;

@Builder
public record ReviewCreatorCertificationRequest(
        @NotNull(message = "인증 상태는 필수입니다.") CreatorCertificationStatus status,
        @NotBlank(message = "심사 메시지는 필수입니다.") String reviewMessage
) {

    public ReviewCreatorCertificationCommand toCommand(Long creatorCertificationId) {
        return new ReviewCreatorCertificationCommand(
                CreatorCertificationId.of(creatorCertificationId),
                status,
                reviewMessage
        );
    }
}

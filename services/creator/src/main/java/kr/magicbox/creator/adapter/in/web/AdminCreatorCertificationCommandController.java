package kr.magicbox.creator.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.creator.adapter.in.web.dto.request.ReviewCertificationRequest;
import kr.magicbox.creator.application.port.in.ReviewCreatorCertificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator/certification")
@RequiredArgsConstructor
public class AdminCreatorCertificationCommandController {

    private final ReviewCreatorCertificationUseCase reviewCreatorCertificationUseCase;

    @PatchMapping("/{creatorCertificationId}/review")
    public ResponseEntity<Void> reviewCertification(
            @PathVariable Long creatorCertificationId,
            @Valid @RequestBody ReviewCertificationRequest request
    ) {
        reviewCreatorCertificationUseCase.reviewCreatorCertification(request.toCommand(creatorCertificationId));
        return ResponseEntity.noContent().build();
    }
}

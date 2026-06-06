package kr.magicbox.review.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.review.adapter.in.web.dto.request.CreateReviewRequest;
import kr.magicbox.review.adapter.in.web.dto.request.UpdateReviewRequest;
import kr.magicbox.review.application.dto.command.DeleteReviewCommand;
import kr.magicbox.review.application.port.in.CreateReviewUseCase;
import kr.magicbox.review.application.port.in.DeleteReviewUseCase;
import kr.magicbox.review.application.port.in.UpdateReviewUseCase;
import kr.magicbox.review.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Validated
public class ReviewCommandController {

    private final CreateReviewUseCase createReviewUseCase;
    private final UpdateReviewUseCase updateReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;

    @PostMapping
    public ResponseEntity<Void> createReview(
            @AuthenticationPrincipal UserId userId,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        createReviewUseCase.createReview(request.toCommand(userId.value()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request
    ) {
        updateReviewUseCase.updateReview(request.toCommand(reviewId, userId.value()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long reviewId
    ) {
        deleteReviewUseCase.deleteReview(new DeleteReviewCommand(reviewId, userId.value()));
        return ResponseEntity.noContent().build();
    }
}

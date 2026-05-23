package kr.magicbox.review.adapter.in.web;

import kr.magicbox.review.application.dto.query.GetReviewsByTargetQuery;
import kr.magicbox.review.application.dto.query.GetReviewsByUserIdQuery;
import kr.magicbox.review.application.dto.result.ReviewResult;
import kr.magicbox.review.application.port.in.GetReviewsByTargetUseCase;
import kr.magicbox.review.application.port.in.GetReviewsByUserIdUseCase;
import kr.magicbox.review.adapter.in.web.dto.response.ReviewResponse;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import kr.magicbox.review.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewQueryController {

    private final GetReviewsByUserIdUseCase getReviewsByUserIdUseCase;
    private final GetReviewsByTargetUseCase getReviewsByTargetUseCase;

    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal UserId userId
    ) {
        List<ReviewResponse> responses = getReviewsByUserIdUseCase.getReviewsByUserId(
                GetReviewsByUserIdQuery.of(UserId.of(userId.value()))
        ).stream()
                .map(review -> ReviewResponse.from(ReviewResult.from(review)))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsByTarget(
            @RequestParam Long targetId,
            @RequestParam ReviewTargetType targetType
    ) {
        List<ReviewResponse> responses = getReviewsByTargetUseCase.getReviewsByTarget(
                GetReviewsByTargetQuery.of(targetId, targetType)
        ).stream()
                .map(review -> ReviewResponse.from(ReviewResult.from(review)))
                .toList();

        return ResponseEntity.ok(responses);
    }
}

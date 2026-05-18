package kr.magicbox.review.application.service;

import kr.magicbox.review.application.dto.query.GetReviewRatingQuery;
import kr.magicbox.review.application.port.in.GetReviewRatingUseCase;
import kr.magicbox.review.application.port.out.ReviewRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetReviewRatingService implements GetReviewRatingUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    public double getReviewRating(GetReviewRatingQuery query) {
        return reviewRepositoryPort.calculateAverageRatingByCreatorId(query.creatorId());
    }
}

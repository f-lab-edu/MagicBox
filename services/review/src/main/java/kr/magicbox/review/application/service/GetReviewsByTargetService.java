package kr.magicbox.review.application.service;

import kr.magicbox.review.application.dto.query.GetReviewsByTargetQuery;
import kr.magicbox.review.application.port.in.GetReviewsByTargetUseCase;
import kr.magicbox.review.application.port.out.ReviewRepositoryPort;
import kr.magicbox.review.domain.aggregate.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetReviewsByTargetService implements GetReviewsByTargetUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    public List<Review> getReviewsByTarget(GetReviewsByTargetQuery query) {
        return reviewRepositoryPort.findAllByTarget(query.targetId(), query.targetType());
    }
}

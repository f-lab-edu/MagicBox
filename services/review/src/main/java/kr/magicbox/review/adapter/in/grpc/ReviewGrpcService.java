package kr.magicbox.review.adapter.in.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import kr.magicbox.review.application.dto.query.GetReviewRatingQuery;
import kr.magicbox.review.application.dto.query.GetReviewsByUserIdQuery;
import kr.magicbox.review.application.port.in.GetReviewRatingUseCase;
import kr.magicbox.review.application.port.in.GetReviewsByUserIdUseCase;
import kr.magicbox.review.domain.vo.CreatorId;
import kr.magicbox.review.domain.vo.UserId;
import kr.magicbox.review.grpc.review.GetAllReviewsByUserIdRequest;
import kr.magicbox.review.grpc.review.GetAllReviewsByUserIdResponse;
import kr.magicbox.review.grpc.review.GetReviewRatingRequest;
import kr.magicbox.review.grpc.review.GetReviewRatingResponse;
import kr.magicbox.review.grpc.review.Review;
import kr.magicbox.review.grpc.review.ReviewServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class ReviewGrpcService extends ReviewServiceGrpc.ReviewServiceImplBase {

    private final GetReviewRatingUseCase getReviewRatingUseCase;
    private final GetReviewsByUserIdUseCase getReviewsByUserIdUseCase;

    @Override
    public void getReviewRating(GetReviewRatingRequest request,
                                StreamObserver<GetReviewRatingResponse> responseObserver) {
        double rating = getReviewRatingUseCase.getReviewRating(
                GetReviewRatingQuery.of(CreatorId.of(request.getCreatorId()))
        );

        responseObserver.onNext(GetReviewRatingResponse.newBuilder()
                .setRating(rating)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllReviewsByUserId(GetAllReviewsByUserIdRequest request,
                                     StreamObserver<GetAllReviewsByUserIdResponse> responseObserver) {
        List<Review> reviews = getReviewsByUserIdUseCase.getReviewsByUserId(
                GetReviewsByUserIdQuery.of(UserId.of(request.getUserId()))
        ).stream()
                .map(review -> Review.newBuilder()
                        .setReviewId(review.getId().value())
                        .setContent(review.getContent())
                        .setCreatedAt(toTimestamp(review.getCreatedAt()))
                        .build())
                .toList();

        responseObserver.onNext(GetAllReviewsByUserIdResponse.newBuilder()
                .addAllReviews(reviews)
                .build());
        responseObserver.onCompleted();
    }

    private Timestamp toTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}

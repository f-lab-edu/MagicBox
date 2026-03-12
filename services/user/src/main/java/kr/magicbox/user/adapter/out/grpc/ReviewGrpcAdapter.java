package kr.magicbox.user.adapter.out.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.user.application.dto.UserReviewDto;
import kr.magicbox.user.application.port.out.port.ReviewPort;
import kr.magicbox.user.global.config.GrpcChannelFactory;
import kr.magicbox.user.global.enums.ServiceHost;
import kr.magicbox.user.global.exception.service.ReviewServiceUnavailableException;
import kr.magicbox.user.grpc.review.GetAllReviewsByUserIdRequest;
import kr.magicbox.user.grpc.review.GetAllReviewsByUserIdResponse;
import kr.magicbox.user.grpc.review.Review;
import kr.magicbox.user.grpc.review.ReviewServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewGrpcAdapter implements ReviewPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "reviewService", fallbackMethod = "getAllReviewsFallback")
    public List<UserReviewDto> getAllReviewsByUserId(Long userId) {
        GetAllReviewsByUserIdRequest request = GetAllReviewsByUserIdRequest.newBuilder()
            .setUserId(userId)
            .build();

        ManagedChannel channel = grpcChannelFactory.getChannel(ServiceHost.REVIEW.getHostName());
        ReviewServiceGrpc.ReviewServiceBlockingStub reviewStub = ReviewServiceGrpc.newBlockingStub(channel);
        GetAllReviewsByUserIdResponse response = reviewStub.getAllReviewsByUserId(request);
        
        return response.getReviewsList().stream()
            .map(this::convertToUserReviewDto)
            .toList();
    }

    @SuppressWarnings("unused") // Resilience4j fallback method signature
    private List<UserReviewDto> getAllReviewsFallback(Long userId, Throwable throwable) {
        log.warn("리뷰 서비스 연결 실패");
        throw new ReviewServiceUnavailableException(userId, throwable);
    }
    
    private UserReviewDto convertToUserReviewDto(Review grpcReview) {
        return UserReviewDto.builder()
            .reviewId(grpcReview.getReviewId())
            .content(grpcReview.getContent())
            .createdAt(Instant.ofEpochSecond(grpcReview.getCreatedAt().getSeconds()))
            .build();
    }
}

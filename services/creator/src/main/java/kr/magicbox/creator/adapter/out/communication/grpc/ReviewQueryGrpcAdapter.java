package kr.magicbox.creator.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.ServiceHost;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.ReviewServiceUnavailableException;
import kr.magicbox.creator.application.dto.result.ReviewRating;
import kr.magicbox.creator.application.port.out.ReviewRatingQueryPort;
import kr.magicbox.creator.grpc.review.GetReviewRatingRequest;
import kr.magicbox.creator.grpc.review.GetReviewRatingResponse;
import kr.magicbox.creator.grpc.review.ReviewServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewQueryGrpcAdapter implements ReviewRatingQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "reviewService", fallbackMethod = "getReviewRatingFallback")
    @TimeLimiter(name = "reviewService", fallbackMethod = "getReviewRatingFallback")
    public CompletableFuture<ReviewRating> getReviewRating(Long creatorId) {
        GetReviewRatingRequest request = GetReviewRatingRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.REVIEW.getHostName());
        ReviewServiceGrpc.ReviewServiceFutureStub stub = ReviewServiceGrpc.newFutureStub(channel);
        ListenableFuture<GetReviewRatingResponse> future = stub.getReviewRating(request);
        try {
            GetReviewRatingResponse response = future.get();
            return CompletableFuture.completedFuture(ReviewRating.of(response.getRating()));
        } catch (ExecutionException e) {
            return CompletableFuture.failedFuture(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    @SuppressWarnings("unused")
    private CompletableFuture<ReviewRating> getReviewRatingFallback(Long creatorId, Throwable throwable) {
        log.warn("리뷰 서비스 연결 실패");
        throw new ReviewServiceUnavailableException(throwable);
    }
}

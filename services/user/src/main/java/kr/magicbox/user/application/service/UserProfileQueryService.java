package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.out.persistence.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.application.dto.UserReviewResult;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import kr.magicbox.user.application.port.out.ReviewQueryPort;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileQueryService implements UserProfileQueryUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final ReviewQueryPort reviewQueryPort;

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResult getUserProfile(String nickname, Long requestUserId) {
        User user = userRepositoryPort.getUserByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);

        List<UserReviewResult> reviews = user.canShowReview() ?
                reviewQueryPort.getAllReviewsByUserId(user.getId()) : Collections.emptyList();
        return GetUserProfileResult.builder()
                .profile(user.getProfile())
                .nickname(user.getNickname())
                .reviews(reviews)
                .role(user.getRole())
                .isMe(user.getId().equals(requestUserId))
                .build();
    }
}

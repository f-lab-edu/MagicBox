package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.application.dto.UserReviewResult;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import kr.magicbox.user.application.port.out.ReviewQueryPort;
import kr.magicbox.user.application.port.out.UserRepositoryOutPort;
import kr.magicbox.user.domain.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileQueryService implements UserProfileQueryUseCase {
    private final UserRepositoryOutPort userRepositoryOutPort;
    private final ReviewQueryPort reviewQueryPort;

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResult getUserProfile(String nickname) {
        User user = userRepositoryOutPort.getUserByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException(nickname));

        List<UserReviewResult> reviews = user.canShowReview() ?
                reviewQueryPort.getAllReviewsByUserId(user.getId()) : Collections.emptyList();
        return GetUserProfileResult.builder()
                .profile(user.getProfile())
                .nickname(user.getNickname())
                .reviews(reviews)
                .role(user.getRole())
                .build();
    }
}

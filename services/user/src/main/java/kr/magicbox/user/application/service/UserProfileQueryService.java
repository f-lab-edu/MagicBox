package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.application.dto.UserReviewDto;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import kr.magicbox.user.application.port.out.ReviewPort;
import kr.magicbox.user.domain.repository.UserRepository;
import kr.magicbox.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileQueryService implements UserProfileQueryUseCase {
    private final UserRepository userRepository;
    private final ReviewPort reviewPort;

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResult getUserProfile(String nickname) {
        UserEntity user = userRepository.getUserByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException(nickname));
        List<UserReviewDto> reviews = user.canShowReview() ?
                reviewPort.getAllReviewsByUserId(user.getId()) : Collections.emptyList();
        return GetUserProfileResult.builder()
                .profile(user.getProfile())
                .nickname(user.getNickname())
                .reviews(reviews)
                .role(user.getRole())
                .build();
    }
}

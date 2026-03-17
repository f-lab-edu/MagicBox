package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.adapter.out.persistence.entity.UserEntity;
import kr.magicbox.user.adapter.out.persistence.mapper.UserMapper;
import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.application.dto.UserReviewResult;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import kr.magicbox.user.application.port.out.ReviewQueryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileQueryService implements UserProfileQueryUseCase {
    private final UserRepository userRepository;
    private final ReviewQueryPort reviewQueryPort;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResult getUserProfile(String nickname) {
        UserEntity userEntity = userRepository.getUserByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException(nickname));

        User user = userMapper.toDomain(userEntity);

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

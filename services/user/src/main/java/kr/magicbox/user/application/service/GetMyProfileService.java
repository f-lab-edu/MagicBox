package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.query.GetMyProfileQuery;
import kr.magicbox.user.application.dto.result.GetMyProfileResult;
import kr.magicbox.user.application.port.in.GetMyProfileUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMyProfileService implements GetMyProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetMyProfileResult getMyProfile(GetMyProfileQuery query) {
        User user = userRepositoryPort.getUserById(query.userId())
                .orElseThrow(UserNotFoundException::new);

        return GetMyProfileResult.builder()
                .id(user.getId().value())
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .role(user.getRole())
                .build();
    }
}

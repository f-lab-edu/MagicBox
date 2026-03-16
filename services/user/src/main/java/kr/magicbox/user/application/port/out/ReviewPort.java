package kr.magicbox.user.application.port.out;

import kr.magicbox.user.application.dto.UserReviewDto;

import java.util.List;

public interface ReviewPort {
    List<UserReviewDto> getAllReviewsByUserId(Long userId);
}

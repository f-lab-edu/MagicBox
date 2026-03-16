package kr.magicbox.user.application.port.out;

import kr.magicbox.user.application.dto.UserReviewResult;

import java.util.List;

public interface ReviewPort {
    List<UserReviewResult> getAllReviewsByUserId(Long userId);
}

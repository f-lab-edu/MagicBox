package kr.magicbox.creator.application.dto.result;

import kr.magicbox.creator.domain.vo.Nickname;

import java.util.List;

public record CreatorMyProfileResult(
        Nickname nickname,
        String tagline,
        long subscriberCount,
        long releaseCount,
        ReviewRating reviewRating,
        List<Object> releases,
        List<Object> shortForms,
        String introduction
) {
}

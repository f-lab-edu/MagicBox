package kr.magicbox.creator.application.dto.result;

import java.util.List;

public record CreatorMyProfileResult(
        String nickname,
        String tagline,
        long subscriberCount,
        long releaseCount,
        ReviewRating reviewRating,
        List<Object> releases,
        List<Object> shortForms,
        String introduction
) {
}

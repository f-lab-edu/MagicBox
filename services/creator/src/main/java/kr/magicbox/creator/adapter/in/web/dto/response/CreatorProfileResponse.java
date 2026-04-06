package kr.magicbox.creator.adapter.in.web.dto.response;

import kr.magicbox.creator.application.dto.result.CreatorPublicProfileResult;
import kr.magicbox.creator.application.dto.result.ReviewRating;
import lombok.Builder;

import java.util.List;

@Builder
public record CreatorProfileResponse(
        String nickname,
        String tagline,
        long subscriberCount,
        long releaseCount,
        ReviewRating reviewRating,
        List<Object> releases,
        List<Object> shortForms,
        String introduction,
        boolean isSubscribed
) {

    public static CreatorProfileResponse from(CreatorPublicProfileResult result) {
        return CreatorProfileResponse.builder()
                .nickname(result.nickname())
                .tagline(result.tagline())
                .subscriberCount(result.subscriberCount())
                .releaseCount(result.releaseCount())
                .reviewRating(result.reviewRating())
                .releases(result.releases())
                .shortForms(result.shortForms())
                .introduction(result.introduction())
                .isSubscribed(result.isSubscribed())
                .build();
    }
}
package kr.magicbox.creator.adapter.in.web.dto.response;

import kr.magicbox.creator.application.dto.result.CreatorMyProfileResult;
import kr.magicbox.creator.application.dto.result.ReviewRating;
import lombok.Builder;

import java.util.List;

@Builder
public record CreatorMyProfileResponse(
        String nickname,
        String tagline,
        long subscriberCount,
        long releaseCount,
        ReviewRating reviewRating,
        List<Object> releases,
        List<Object> shortForms,
        String introduction
) {

    public static CreatorMyProfileResponse from(CreatorMyProfileResult result) {
        return CreatorMyProfileResponse.builder()
                .nickname(result.nickname())
                .tagline(result.tagline())
                .subscriberCount(result.subscriberCount())
                .releaseCount(result.releaseCount())
                .reviewRating(result.reviewRating())
                .releases(result.releases())
                .shortForms(result.shortForms())
                .introduction(result.introduction())
                .build();
    }
}
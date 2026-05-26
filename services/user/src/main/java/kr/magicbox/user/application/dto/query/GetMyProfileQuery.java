package kr.magicbox.user.application.dto.query;

import kr.magicbox.user.domain.vo.UserId;
import lombok.Builder;

@Builder
public record GetMyProfileQuery(
        UserId userId
) {
    public static GetMyProfileQuery of(UserId userId) {
        return new GetMyProfileQuery(userId);
    }
}

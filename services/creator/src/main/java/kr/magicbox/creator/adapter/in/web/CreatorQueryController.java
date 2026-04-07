package kr.magicbox.creator.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.creator.adapter.in.web.constants.CursorConstants;
import kr.magicbox.creator.adapter.in.web.dto.response.CreatorMyProfileResponse;
import kr.magicbox.creator.adapter.in.web.dto.response.CreatorProfileResponse;
import kr.magicbox.creator.adapter.in.web.dto.response.CreatorSearchResponse;
import kr.magicbox.creator.adapter.in.web.dto.response.CursorResponse;
import kr.magicbox.creator.adapter.in.web.validation.CursorSize;
import kr.magicbox.creator.application.dto.result.CreatorPublicProfileResult;
import kr.magicbox.creator.application.dto.query.GetAllCreatorsQuery;
import kr.magicbox.creator.application.dto.query.GetCreatorProfileQuery;
import kr.magicbox.creator.application.dto.query.GetMyCreatorProfileQuery;
import kr.magicbox.creator.application.dto.query.SearchCreatorsQuery;
import kr.magicbox.creator.application.port.in.GetAllCreatorsUseCase;
import kr.magicbox.creator.application.port.in.GetCreatorProfileUseCase;
import kr.magicbox.creator.application.port.in.GetMyCreatorProfileUseCase;
import kr.magicbox.creator.application.port.in.SearchCreatorsUseCase;
import kr.magicbox.creator.domain.vo.Nickname;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.ToLongFunction;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
@Validated
public class CreatorQueryController {

    private final GetCreatorProfileUseCase getCreatorProfileUseCase;
    private final GetMyCreatorProfileUseCase getMyCreatorProfileUseCase;
    private final GetAllCreatorsUseCase getAllCreatorsUseCase;
    private final SearchCreatorsUseCase searchCreatorsUseCase;

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<CreatorProfileResponse> getProfile(
            @AuthenticationPrincipal UserId userId,
            @PathVariable String nickname
    ) {
        CreatorPublicProfileResult result = getCreatorProfileUseCase.getCreatorProfile(
                GetCreatorProfileQuery.of(Nickname.of(nickname), userId)
        );
        return ResponseEntity.ok(CreatorProfileResponse.builder()
                .nickname(result.nickname())
                .tagline(result.tagline())
                .subscriberCount(result.subscriberCount())
                .releaseCount(result.releaseCount())
                .reviewRating(result.reviewRating())
                .releases(result.releases())
                .shortForms(result.shortForms())
                .introduction(result.introduction())
                .isSubscribed(result.isSubscribed())
                .build());
    }

    @GetMapping("/profile/me")
    public ResponseEntity<CreatorMyProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserId userId
    ) {
        var result = getMyCreatorProfileUseCase.getMyCreatorProfile(GetMyCreatorProfileQuery.of(userId));
        return ResponseEntity.ok(CreatorMyProfileResponse.builder()
                .nickname(result.nickname())
                .tagline(result.tagline())
                .subscriberCount(result.subscriberCount())
                .releaseCount(result.releaseCount())
                .reviewRating(result.reviewRating())
                .releases(result.releases())
                .shortForms(result.shortForms())
                .introduction(result.introduction())
                .build());
    }

    @GetMapping
    public ResponseEntity<CursorResponse<CreatorSearchResponse>> getAllCreators(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) @CursorSize Integer size) {
        List<CreatorSearchResponse> content = getAllCreatorsUseCase.getAllCreators(GetAllCreatorsQuery.of(cursor, size + 1))
                .stream()
                .map(this::toCreatorSearchResponse)
                .toList();
        return ResponseEntity.ok(buildCursorResponse(content, size, CreatorSearchResponse::creatorId));
    }

    @GetMapping("/search")
    public ResponseEntity<CursorResponse<CreatorSearchResponse>> searchCreators(
            @RequestParam @NotBlank(message = "닉네임은 필수입니다.") String nickname,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) @CursorSize Integer size) {
        List<CreatorSearchResponse> content = searchCreatorsUseCase.searchCreators(SearchCreatorsQuery.of(nickname, cursor, size + 1))
                .stream()
                .map(this::toCreatorSearchResponse)
                .toList();
        return ResponseEntity.ok(buildCursorResponse(content, size, CreatorSearchResponse::creatorId));
    }

    private CreatorSearchResponse toCreatorSearchResponse(kr.magicbox.creator.application.dto.result.CreatorSearchResult result) {
        return CreatorSearchResponse.builder()
                .creatorId(result.creatorId().value())
                .nickname(result.nickname())
                .introduction(result.introduction())
                .profileImageUrl(result.profileImageUrl())
                .tagline(result.tagline())
                .build();
    }

    private <T> CursorResponse<T> buildCursorResponse(List<T> content, int size, ToLongFunction<T> cursorExtractor) {
        boolean hasNext = content.size() > size;
        List<T> sliced = hasNext ? content.subList(0, size) : content;
        Long nextCursor = hasNext ? cursorExtractor.applyAsLong(sliced.getLast()) : null;
        return CursorResponse.<T>builder()
                .content(sliced)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}

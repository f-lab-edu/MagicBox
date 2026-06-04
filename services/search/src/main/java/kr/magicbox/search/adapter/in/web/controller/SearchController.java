package kr.magicbox.search.adapter.in.web.controller;

import kr.magicbox.search.adapter.in.web.constants.CursorConstants;
import kr.magicbox.search.adapter.in.web.dto.response.CreatorSearchResponse;
import kr.magicbox.search.adapter.in.web.dto.response.GeneralGoodsSearchResponse;
import kr.magicbox.search.adapter.in.web.dto.response.PageResponse;
import kr.magicbox.search.adapter.in.web.dto.response.ReleaseSearchResponse;
import kr.magicbox.search.adapter.in.web.validation.CursorSize;
import kr.magicbox.search.application.dto.query.SearchCreatorsQuery;
import kr.magicbox.search.application.dto.query.SearchGeneralGoodsQuery;
import kr.magicbox.search.application.dto.query.SearchReleasesQuery;
import kr.magicbox.search.application.port.in.HistoryUseCase;
import kr.magicbox.search.application.port.in.PopularQueryUseCase;
import kr.magicbox.search.application.port.in.RecentQueryUseCase;
import kr.magicbox.search.application.port.in.SearchAllUseCase;
import kr.magicbox.search.application.port.in.SearchCreatorsUseCase;
import kr.magicbox.search.application.port.in.SearchGeneralGoodsUseCase;
import kr.magicbox.search.application.port.in.SearchReleasesUseCase;
import kr.magicbox.search.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
public class SearchController {

    private final SearchCreatorsUseCase searchCreatorsUseCase;
    private final SearchReleasesUseCase searchReleasesUseCase;
    private final SearchGeneralGoodsUseCase searchGeneralGoodsUseCase;
    private final SearchAllUseCase searchAllUseCase;
    private final PopularQueryUseCase popularQueryUseCase;
    private final RecentQueryUseCase recentQueryUseCase;
    private final HistoryUseCase historyUseCase;

    // ===== 검색 =====

    @GetMapping("/creators")
    public Mono<ResponseEntity<PageResponse<CreatorSearchResponse>>> searchCreators(
            @AuthenticationPrincipal UserId userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @CursorSize @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) int size
    ) {
        return searchCreatorsUseCase.searchCreators(SearchCreatorsQuery.of(userId.value(), keyword, page, size))
                .map(CreatorSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, page, size)));
    }

    @GetMapping("/releases")
    public Mono<ResponseEntity<PageResponse<ReleaseSearchResponse>>> searchReleases(
            @AuthenticationPrincipal UserId userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @CursorSize @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) int size
    ) {
        return searchReleasesUseCase.searchReleases(SearchReleasesQuery.of(userId.value(), keyword, page, size))
                .map(ReleaseSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, page, size)));
    }

    @GetMapping("/general-goods")
    public Mono<ResponseEntity<PageResponse<GeneralGoodsSearchResponse>>> searchGeneralGoods(
            @AuthenticationPrincipal UserId userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @CursorSize @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) int size
    ) {
        return searchGeneralGoodsUseCase.searchGeneralGoods(SearchGeneralGoodsQuery.of(userId.value(), keyword, page, size))
                .map(GeneralGoodsSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, page, size)));
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<Map<String, Object>>> searchAll(
            @AuthenticationPrincipal UserId userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @CursorSize @RequestParam(defaultValue = CursorConstants.DEFAULT_SIZE) int size
    ) {
        return searchAllUseCase.searchAll(SearchCreatorsQuery.of(userId.value(), keyword, page, size))
                .map(result -> ResponseEntity.ok(Map.of(
                        "creators", PageResponse.of(result.creators().stream().map(CreatorSearchResponse::from).toList(), page, size),
                        "releases", PageResponse.of(result.releases().stream().map(ReleaseSearchResponse::from).toList(), page, size),
                        "generalGoods", PageResponse.of(result.generalGoods().stream().map(GeneralGoodsSearchResponse::from).toList(), page, size)
                )));
    }

    // ===== 인기 =====

    @GetMapping("/popular/creators")
    public Mono<ResponseEntity<PageResponse<CreatorSearchResponse>>> getPopularCreators() {
        return popularQueryUseCase.getPopularCreators()
                .map(CreatorSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, 0, list.size())));
    }

    @GetMapping("/popular/releases")
    public Mono<ResponseEntity<PageResponse<ReleaseSearchResponse>>> getPopularReleases() {
        return popularQueryUseCase.getPopularReleases()
                .map(ReleaseSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, 0, list.size())));
    }

    @GetMapping("/popular/general-goods")
    public Mono<ResponseEntity<PageResponse<GeneralGoodsSearchResponse>>> getPopularGeneralGoods() {
        return popularQueryUseCase.getPopularGeneralGoods()
                .map(GeneralGoodsSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, 0, list.size())));
    }

    // ===== 최신 =====

    @GetMapping("/recent/creators")
    public Mono<ResponseEntity<PageResponse<CreatorSearchResponse>>> getRecentCreators() {
        return recentQueryUseCase.getRecentCreators()
                .map(CreatorSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, 0, list.size())));
    }

    @GetMapping("/recent/releases")
    public Mono<ResponseEntity<PageResponse<ReleaseSearchResponse>>> getRecentReleases() {
        return recentQueryUseCase.getRecentReleases()
                .map(ReleaseSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, 0, list.size())));
    }

    @GetMapping("/recent/general-goods")
    public Mono<ResponseEntity<PageResponse<GeneralGoodsSearchResponse>>> getRecentGeneralGoods() {
        return recentQueryUseCase.getRecentGeneralGoods()
                .map(GeneralGoodsSearchResponse::from)
                .collectList()
                .map(list -> ResponseEntity.ok(PageResponse.of(list, 0, list.size())));
    }

    // ===== 이력 =====

    @PostMapping("/history/creators/{creatorId}")
    public Mono<ResponseEntity<Void>> recordViewedCreator(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long creatorId
    ) {
        return historyUseCase.recordViewedCreator(userId.value(), creatorId)
                .thenReturn(ResponseEntity.<Void>ok().build());
    }

    @GetMapping("/history/creators")
    public Mono<ResponseEntity<List<CreatorSearchResponse>>> getViewedCreators(
            @AuthenticationPrincipal UserId userId
    ) {
        return historyUseCase.getViewedCreators(userId.value())
                .map(CreatorSearchResponse::from)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/history/queries")
    public Mono<ResponseEntity<List<String>>> getSearchQueries(
            @AuthenticationPrincipal UserId userId
    ) {
        return historyUseCase.getSearchQueries(userId.value())
                .collectList()
                .map(ResponseEntity::ok);
    }
}

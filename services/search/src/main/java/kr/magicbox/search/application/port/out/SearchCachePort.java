package kr.magicbox.search.application.port.out;

import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SearchCachePort {

    // Cache Aside - 인기 목록
    Mono<List<CreatorSearchResult>> getPopularCreators();
    Mono<Void> setPopularCreators(List<CreatorSearchResult> creators);

    Mono<List<ReleaseSearchResult>> getPopularReleases();
    Mono<Void> setPopularReleases(List<ReleaseSearchResult> releases);

    Mono<List<GeneralGoodsSearchResult>> getPopularGeneralGoods();
    Mono<Void> setPopularGeneralGoods(List<GeneralGoodsSearchResult> goods);

    // Write Through - 최신 목록
    Mono<Void> addRecentCreator(CreatorDocument document);
    Flux<CreatorDocument> getRecentCreators();

    Mono<Void> addRecentRelease(ReleaseDocument document);
    Flux<ReleaseDocument> getRecentReleases();

    Mono<Void> addRecentGeneralGoods(GeneralGoodsDocument document);
    Flux<GeneralGoodsDocument> getRecentGeneralGoods();

    // Write Through - 개인화 이력
    Mono<Void> addViewedCreator(Long userId, CreatorDocument document);
    Flux<CreatorDocument> getViewedCreators(Long userId);

    Mono<Void> addSearchQuery(Long userId, String query);
    Flux<String> getSearchQueries(Long userId);
}

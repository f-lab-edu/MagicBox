package kr.magicbox.search.application.port.out;

import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;

import java.util.List;
import java.util.Optional;

public interface SearchCachePort {

    // Cache Aside - 인기 목록
    Optional<List<CreatorSearchResult>> getPopularCreators();
    void setPopularCreators(List<CreatorSearchResult> creators);

    Optional<List<ReleaseSearchResult>> getPopularReleases();
    void setPopularReleases(List<ReleaseSearchResult> releases);

    Optional<List<GeneralGoodsSearchResult>> getPopularGeneralGoods();
    void setPopularGeneralGoods(List<GeneralGoodsSearchResult> goods);

    // Write Through - 최신 목록
    void addRecentCreator(CreatorDocument document);
    List<CreatorDocument> getRecentCreators();

    void addRecentRelease(ReleaseDocument document);
    List<ReleaseDocument> getRecentReleases();

    void addRecentGeneralGoods(GeneralGoodsDocument document);
    List<GeneralGoodsDocument> getRecentGeneralGoods();

    // Write Through - 개인화 이력
    void addViewedCreator(Long userId, CreatorDocument document);
    List<CreatorDocument> getViewedCreators(Long userId);

    void addSearchQuery(Long userId, String query);
    List<String> getSearchQueries(Long userId);
}

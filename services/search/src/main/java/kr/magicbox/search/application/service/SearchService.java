package kr.magicbox.search.application.service;

import kr.magicbox.search.application.dto.query.SearchCreatorsQuery;
import kr.magicbox.search.application.dto.query.SearchGeneralGoodsQuery;
import kr.magicbox.search.application.dto.query.SearchReleasesQuery;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import kr.magicbox.search.application.dto.result.SearchAllResult;
import kr.magicbox.search.application.port.in.SearchAllUseCase;
import kr.magicbox.search.application.port.in.SearchCreatorsUseCase;
import kr.magicbox.search.application.port.in.SearchGeneralGoodsUseCase;
import kr.magicbox.search.application.port.in.SearchReleasesUseCase;
import kr.magicbox.search.application.port.out.CreatorIndexPort;
import kr.magicbox.search.application.port.out.GeneralGoodsIndexPort;
import kr.magicbox.search.application.port.out.ReleaseIndexPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchCreatorsUseCase, SearchReleasesUseCase, SearchGeneralGoodsUseCase, SearchAllUseCase {

    private final CreatorIndexPort creatorIndexPort;
    private final ReleaseIndexPort releaseIndexPort;
    private final GeneralGoodsIndexPort generalGoodsIndexPort;

    @Override
    public List<CreatorSearchResult> searchCreators(SearchCreatorsQuery query) {
        return creatorIndexPort.search(query.keyword(), query.page(), query.size() + 1).stream()
                .map(CreatorSearchResult::from)
                .toList();
    }

    @Override
    public List<ReleaseSearchResult> searchReleases(SearchReleasesQuery query) {
        return releaseIndexPort.search(query.keyword(), query.page(), query.size() + 1).stream()
                .map(ReleaseSearchResult::from)
                .toList();
    }

    @Override
    public List<GeneralGoodsSearchResult> searchGeneralGoods(SearchGeneralGoodsQuery query) {
        return generalGoodsIndexPort.search(query.keyword(), query.page(), query.size() + 1).stream()
                .map(GeneralGoodsSearchResult::from)
                .toList();
    }

    @Override
    public SearchAllResult searchAll(SearchCreatorsQuery query) {
        return new SearchAllResult(
                searchCreators(query),
                searchReleases(SearchReleasesQuery.of(query.keyword(), query.page(), query.size())),
                searchGeneralGoods(SearchGeneralGoodsQuery.of(query.keyword(), query.page(), query.size()))
        );
    }
}

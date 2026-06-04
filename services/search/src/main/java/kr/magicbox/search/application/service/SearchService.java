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
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchCreatorsUseCase, SearchReleasesUseCase, SearchGeneralGoodsUseCase, SearchAllUseCase {

    private final CreatorIndexPort creatorIndexPort;
    private final ReleaseIndexPort releaseIndexPort;
    private final GeneralGoodsIndexPort generalGoodsIndexPort;
    private final SearchCachePort searchCachePort;

    @Override
    public Flux<CreatorSearchResult> searchCreators(SearchCreatorsQuery query) {
        return searchCachePort.addSearchQuery(query.userId(), query.keyword())
                .thenMany(creatorIndexPort.search(query.keyword(), query.page(), query.size() + 1)
                        .map(CreatorSearchResult::from));
    }

    @Override
    public Flux<ReleaseSearchResult> searchReleases(SearchReleasesQuery query) {
        return searchCachePort.addSearchQuery(query.userId(), query.keyword())
                .thenMany(releaseIndexPort.search(query.keyword(), query.page(), query.size() + 1)
                        .map(ReleaseSearchResult::from));
    }

    @Override
    public Flux<GeneralGoodsSearchResult> searchGeneralGoods(SearchGeneralGoodsQuery query) {
        return searchCachePort.addSearchQuery(query.userId(), query.keyword())
                .thenMany(generalGoodsIndexPort.search(query.keyword(), query.page(), query.size() + 1)
                        .map(GeneralGoodsSearchResult::from));
    }

    @Override
    public Mono<SearchAllResult> searchAll(SearchCreatorsQuery query) {
        return searchCachePort.addSearchQuery(query.userId(), query.keyword())
                .then(Mono.zip(
                        creatorIndexPort.search(query.keyword(), query.page(), query.size() + 1).map(CreatorSearchResult::from).collectList(),
                        releaseIndexPort.search(query.keyword(), query.page(), query.size() + 1).map(ReleaseSearchResult::from).collectList(),
                        generalGoodsIndexPort.search(query.keyword(), query.page(), query.size() + 1).map(GeneralGoodsSearchResult::from).collectList()
                ).map(tuple -> new SearchAllResult(tuple.getT1(), tuple.getT2(), tuple.getT3())));
    }
}

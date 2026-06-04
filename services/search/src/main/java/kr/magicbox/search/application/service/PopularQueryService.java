package kr.magicbox.search.application.service;

import kr.magicbox.search.adapter.out.cache.CacheProperties;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import kr.magicbox.search.application.port.in.PopularQueryUseCase;
import kr.magicbox.search.application.port.out.CreatorIndexPort;
import kr.magicbox.search.application.port.out.GeneralGoodsIndexPort;
import kr.magicbox.search.application.port.out.ReleaseIndexPort;
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PopularQueryService implements PopularQueryUseCase {

    private final CreatorIndexPort creatorIndexPort;
    private final ReleaseIndexPort releaseIndexPort;
    private final GeneralGoodsIndexPort generalGoodsIndexPort;
    private final SearchCachePort searchCachePort;
    private final CacheProperties cacheProperties;

    @Override
    public Flux<CreatorSearchResult> getPopularCreators() {
        return searchCachePort.getPopularCreators()
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(creatorIndexPort.findPopular(cacheProperties.getPopularSize())
                        .map(CreatorSearchResult::from)
                        .collectList()
                        .flatMap(list -> searchCachePort.setPopularCreators(list).thenReturn(list))
                        .flatMapMany(Flux::fromIterable));
    }

    @Override
    public Flux<ReleaseSearchResult> getPopularReleases() {
        return searchCachePort.getPopularReleases()
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(releaseIndexPort.findPopular(cacheProperties.getPopularSize())
                        .map(ReleaseSearchResult::from)
                        .collectList()
                        .flatMap(list -> searchCachePort.setPopularReleases(list).thenReturn(list))
                        .flatMapMany(Flux::fromIterable));
    }

    @Override
    public Flux<GeneralGoodsSearchResult> getPopularGeneralGoods() {
        return searchCachePort.getPopularGeneralGoods()
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(generalGoodsIndexPort.findPopular(cacheProperties.getPopularSize())
                        .map(GeneralGoodsSearchResult::from)
                        .collectList()
                        .flatMap(list -> searchCachePort.setPopularGeneralGoods(list).thenReturn(list))
                        .flatMapMany(Flux::fromIterable));
    }
}

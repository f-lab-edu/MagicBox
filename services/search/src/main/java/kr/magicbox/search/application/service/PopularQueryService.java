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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularQueryService implements PopularQueryUseCase {

    private final CreatorIndexPort creatorIndexPort;
    private final ReleaseIndexPort releaseIndexPort;
    private final GeneralGoodsIndexPort generalGoodsIndexPort;
    private final SearchCachePort searchCachePort;
    private final CacheProperties cacheProperties;

    @Override
    public List<CreatorSearchResult> getPopularCreators() {
        return searchCachePort.getPopularCreators().orElseGet(() -> {
            List<CreatorSearchResult> result = creatorIndexPort.findPopular(cacheProperties.getPopularSize()).stream()
                    .map(CreatorSearchResult::from).toList();
            searchCachePort.setPopularCreators(result);
            return result;
        });
    }

    @Override
    public List<ReleaseSearchResult> getPopularReleases() {
        return searchCachePort.getPopularReleases().orElseGet(() -> {
            List<ReleaseSearchResult> result = releaseIndexPort.findPopular(cacheProperties.getPopularSize()).stream()
                    .map(ReleaseSearchResult::from).toList();
            searchCachePort.setPopularReleases(result);
            return result;
        });
    }

    @Override
    public List<GeneralGoodsSearchResult> getPopularGeneralGoods() {
        return searchCachePort.getPopularGeneralGoods().orElseGet(() -> {
            List<GeneralGoodsSearchResult> result = generalGoodsIndexPort.findPopular(cacheProperties.getPopularSize()).stream()
                    .map(GeneralGoodsSearchResult::from).toList();
            searchCachePort.setPopularGeneralGoods(result);
            return result;
        });
    }
}

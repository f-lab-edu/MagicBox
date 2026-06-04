package kr.magicbox.search.application.service;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import kr.magicbox.search.application.port.in.RecentQueryUseCase;
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RecentQueryService implements RecentQueryUseCase {

    private final SearchCachePort searchCachePort;

    @Override
    public Flux<CreatorSearchResult> getRecentCreators() {
        return searchCachePort.getRecentCreators().map(CreatorSearchResult::from);
    }

    @Override
    public Flux<ReleaseSearchResult> getRecentReleases() {
        return searchCachePort.getRecentReleases().map(ReleaseSearchResult::from);
    }

    @Override
    public Flux<GeneralGoodsSearchResult> getRecentGeneralGoods() {
        return searchCachePort.getRecentGeneralGoods().map(GeneralGoodsSearchResult::from);
    }
}

package kr.magicbox.search.application.service;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import kr.magicbox.search.application.port.in.RecentQueryUseCase;
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentQueryService implements RecentQueryUseCase {

    private final SearchCachePort searchCachePort;

    @Override
    public List<CreatorSearchResult> getRecentCreators() {
        return searchCachePort.getRecentCreators().stream().map(CreatorSearchResult::from).toList();
    }

    @Override
    public List<ReleaseSearchResult> getRecentReleases() {
        return searchCachePort.getRecentReleases().stream().map(ReleaseSearchResult::from).toList();
    }

    @Override
    public List<GeneralGoodsSearchResult> getRecentGeneralGoods() {
        return searchCachePort.getRecentGeneralGoods().stream().map(GeneralGoodsSearchResult::from).toList();
    }
}

package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;

import java.util.List;

public interface RecentQueryUseCase {
    List<CreatorSearchResult> getRecentCreators();
    List<ReleaseSearchResult> getRecentReleases();
    List<GeneralGoodsSearchResult> getRecentGeneralGoods();
}

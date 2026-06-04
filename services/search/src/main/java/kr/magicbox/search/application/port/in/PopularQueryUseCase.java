package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import reactor.core.publisher.Flux;

public interface PopularQueryUseCase {
    Flux<CreatorSearchResult> getPopularCreators();
    Flux<ReleaseSearchResult> getPopularReleases();
    Flux<GeneralGoodsSearchResult> getPopularGeneralGoods();
}

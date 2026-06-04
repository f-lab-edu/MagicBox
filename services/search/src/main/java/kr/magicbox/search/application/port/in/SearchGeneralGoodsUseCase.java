package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchGeneralGoodsQuery;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;

import reactor.core.publisher.Flux;

public interface SearchGeneralGoodsUseCase {
    Flux<GeneralGoodsSearchResult> searchGeneralGoods(SearchGeneralGoodsQuery query);
}

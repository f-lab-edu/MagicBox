package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchGeneralGoodsQuery;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;

import java.util.List;

public interface SearchGeneralGoodsUseCase {
    List<GeneralGoodsSearchResult> searchGeneralGoods(SearchGeneralGoodsQuery query);
}

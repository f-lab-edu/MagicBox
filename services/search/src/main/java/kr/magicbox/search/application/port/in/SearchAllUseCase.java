package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchCreatorsQuery;
import kr.magicbox.search.application.dto.result.SearchAllResult;

public interface SearchAllUseCase {
    SearchAllResult searchAll(SearchCreatorsQuery query);
}

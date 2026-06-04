package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchCreatorsQuery;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;

import java.util.List;

public interface SearchCreatorsUseCase {
    List<CreatorSearchResult> searchCreators(SearchCreatorsQuery query);
}

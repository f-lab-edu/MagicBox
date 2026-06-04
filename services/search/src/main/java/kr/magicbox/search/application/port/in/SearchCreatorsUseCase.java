package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchCreatorsQuery;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import reactor.core.publisher.Flux;

public interface SearchCreatorsUseCase {
    Flux<CreatorSearchResult> searchCreators(SearchCreatorsQuery query);
}

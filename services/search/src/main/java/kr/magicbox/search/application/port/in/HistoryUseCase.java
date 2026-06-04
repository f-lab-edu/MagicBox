package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HistoryUseCase {
    Mono<Void> recordViewedCreator(Long userId, Long creatorId);
    Flux<CreatorSearchResult> getViewedCreators(Long userId);
    Flux<String> getSearchQueries(Long userId);
}

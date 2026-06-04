package kr.magicbox.search.application.service;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.port.in.HistoryUseCase;
import kr.magicbox.search.application.port.out.CreatorIndexPort;
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryUseCase {

    private final CreatorIndexPort creatorIndexPort;
    private final SearchCachePort searchCachePort;

    @Override
    public Mono<Void> recordViewedCreator(Long userId, Long creatorId) {
        return creatorIndexPort.findByCreatorId(creatorId)
                .flatMap(doc -> searchCachePort.addViewedCreator(userId, doc));
    }

    @Override
    public Flux<CreatorSearchResult> getViewedCreators(Long userId) {
        return searchCachePort.getViewedCreators(userId).map(CreatorSearchResult::from);
    }

    @Override
    public Flux<String> getSearchQueries(Long userId) {
        return searchCachePort.getSearchQueries(userId);
    }
}

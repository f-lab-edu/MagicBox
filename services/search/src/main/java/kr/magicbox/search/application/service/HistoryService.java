package kr.magicbox.search.application.service;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.port.in.HistoryUseCase;
import kr.magicbox.search.application.port.out.CreatorIndexPort;
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryUseCase {

    private final CreatorIndexPort creatorIndexPort;
    private final SearchCachePort searchCachePort;

    @Override
    public void recordViewedCreator(Long userId, Long creatorId) {
        creatorIndexPort.findByCreatorId(creatorId).ifPresent(doc ->
                searchCachePort.addViewedCreator(userId, doc)
        );
    }

    @Override
    public List<CreatorSearchResult> getViewedCreators(Long userId) {
        return searchCachePort.getViewedCreators(userId).stream().map(CreatorSearchResult::from).toList();
    }

    @Override
    public List<String> getSearchQueries(Long userId) {
        return searchCachePort.getSearchQueries(userId);
    }
}

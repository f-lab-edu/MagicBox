package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;

import java.util.List;

public interface HistoryUseCase {
    void recordViewedCreator(Long userId, Long creatorId);
    List<CreatorSearchResult> getViewedCreators(Long userId);
    List<String> getSearchQueries(Long userId);
}

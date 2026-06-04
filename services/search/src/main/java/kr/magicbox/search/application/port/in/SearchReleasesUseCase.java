package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchReleasesQuery;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;

import reactor.core.publisher.Flux;

public interface SearchReleasesUseCase {
    Flux<ReleaseSearchResult> searchReleases(SearchReleasesQuery query);
}

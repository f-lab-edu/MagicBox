package kr.magicbox.search.application.port.in;

import kr.magicbox.search.application.dto.query.SearchReleasesQuery;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;

import java.util.List;

public interface SearchReleasesUseCase {
    List<ReleaseSearchResult> searchReleases(SearchReleasesQuery query);
}

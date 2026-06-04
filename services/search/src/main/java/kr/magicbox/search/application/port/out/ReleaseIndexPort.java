package kr.magicbox.search.application.port.out;

import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReleaseIndexPort {
    Mono<Void> save(ReleaseDocument document);
    Mono<Void> update(Long releaseId, String title, String description, List<String> mediaUrls);
    Mono<Void> delete(Long releaseId);
    Flux<ReleaseDocument> search(String keyword, int page, int size);
    Flux<ReleaseDocument> findPopular(int size);
    Flux<ReleaseDocument> findRecent(int size);
}

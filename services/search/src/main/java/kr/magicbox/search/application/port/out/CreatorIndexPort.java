package kr.magicbox.search.application.port.out;

import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreatorIndexPort {
    Mono<CreatorDocument> findByCreatorId(Long creatorId);
    Mono<Void> save(CreatorDocument document);
    Mono<Void> update(Long creatorId, String nickname, String tagline, String profileImageUrl);
    Mono<Void> delete(Long creatorId);
    Flux<CreatorDocument> search(String keyword, int page, int size);
    Flux<CreatorDocument> findPopular(int size);
    Flux<CreatorDocument> findRecent(int size);
}

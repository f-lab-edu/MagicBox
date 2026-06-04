package kr.magicbox.search.adapter.out.elasticsearch.repository;

import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;

public interface CreatorElasticsearchRepository extends ReactiveElasticsearchRepository<CreatorDocument, String> {
    Mono<CreatorDocument> findByCreatorId(Long creatorId);
    Mono<Void> deleteByCreatorId(Long creatorId);
}

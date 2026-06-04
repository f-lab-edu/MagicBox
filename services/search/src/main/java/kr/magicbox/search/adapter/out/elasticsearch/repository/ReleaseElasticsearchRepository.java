package kr.magicbox.search.adapter.out.elasticsearch.repository;

import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;

public interface ReleaseElasticsearchRepository extends ReactiveElasticsearchRepository<ReleaseDocument, String> {
    Mono<ReleaseDocument> findByReleaseId(Long releaseId);
    Mono<Void> deleteByReleaseId(Long releaseId);
}

package kr.magicbox.search.adapter.out.elasticsearch.repository;

import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Mono;

public interface GeneralGoodsElasticsearchRepository extends ReactiveElasticsearchRepository<GeneralGoodsDocument, String> {
    Mono<GeneralGoodsDocument> findByGeneralGoodsId(Long generalGoodsId);
    Mono<Void> deleteByGeneralGoodsId(Long generalGoodsId);
}

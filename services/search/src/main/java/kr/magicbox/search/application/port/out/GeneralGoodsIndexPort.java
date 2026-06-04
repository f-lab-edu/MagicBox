package kr.magicbox.search.application.port.out;

import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GeneralGoodsIndexPort {
    Mono<Void> save(GeneralGoodsDocument document);
    Mono<Void> update(Long generalGoodsId, String name, Long price, Long stock, List<String> mediaUrls);
    Mono<Void> delete(Long generalGoodsId);
    Flux<GeneralGoodsDocument> search(String keyword, int page, int size);
    Flux<GeneralGoodsDocument> findPopular(int size);
    Flux<GeneralGoodsDocument> findRecent(int size);
}

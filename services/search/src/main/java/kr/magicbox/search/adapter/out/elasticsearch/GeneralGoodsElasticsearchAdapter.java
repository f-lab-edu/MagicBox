package kr.magicbox.search.adapter.out.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import kr.magicbox.search.adapter.out.elasticsearch.repository.GeneralGoodsElasticsearchRepository;
import kr.magicbox.search.application.port.out.GeneralGoodsIndexPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GeneralGoodsElasticsearchAdapter implements GeneralGoodsIndexPort {

    private static final String INDEX = "general-goods-index";

    private final GeneralGoodsElasticsearchRepository generalGoodsElasticsearchRepository;
    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    @Override
    public Mono<Void> save(GeneralGoodsDocument document) {
        return generalGoodsElasticsearchRepository.save(document).then();
    }

    @Override
    public Mono<Void> update(Long generalGoodsId, String name, Long price, Long stock, List<String> mediaUrls) {
        return generalGoodsElasticsearchRepository.findByGeneralGoodsId(generalGoodsId)
                .flatMap(doc -> save(GeneralGoodsDocument.builder()
                        .id(doc.getId())
                        .generalGoodsId(doc.getGeneralGoodsId())
                        .creatorId(doc.getCreatorId())
                        .name(name != null ? name : doc.getName())
                        .price(price != null ? price : doc.getPrice())
                        .stock(stock != null ? stock : doc.getStock())
                        .mediaUrls(mediaUrls != null ? mediaUrls : doc.getMediaUrls())
                        .createdAt(doc.getCreatedAt())
                        .build()));
    }

    @Override
    public Mono<Void> delete(Long generalGoodsId) {
        return generalGoodsElasticsearchRepository.deleteByGeneralGoodsId(generalGoodsId);
    }

    @Override
    public Flux<GeneralGoodsDocument> search(String keyword, int page, int size) {
        return Mono.fromFuture(() -> elasticsearchAsyncClient.search(s -> s
                        .index(INDEX)
                        .from(page * size)
                        .size(size)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(keyword)
                                        .fields("name")
                                        .analyzer("nori")
                                )
                        ),
                GeneralGoodsDocument.class
        )).flatMapMany(response -> Flux.fromIterable(response.hits().hits()).mapNotNull(Hit::source));
    }

    @Override
    public Flux<GeneralGoodsDocument> findPopular(int size) {
        return findSortedByCreatedAt(size);
    }

    @Override
    public Flux<GeneralGoodsDocument> findRecent(int size) {
        return findSortedByCreatedAt(size);
    }

    private Flux<GeneralGoodsDocument> findSortedByCreatedAt(int size) {
        return Mono.fromFuture(() -> elasticsearchAsyncClient.search(s -> s
                        .index(INDEX)
                        .size(size)
                        .sort(sort -> sort.field(f -> f.field("created_at").order(SortOrder.Desc))),
                GeneralGoodsDocument.class
        )).flatMapMany(response -> Flux.fromIterable(response.hits().hits()).mapNotNull(Hit::source));
    }
}

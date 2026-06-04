package kr.magicbox.search.adapter.out.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import kr.magicbox.search.adapter.out.elasticsearch.repository.ReleaseElasticsearchRepository;
import kr.magicbox.search.application.port.out.ReleaseIndexPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReleaseElasticsearchAdapter implements ReleaseIndexPort {

    private static final String INDEX = "release-index";

    private final ReleaseElasticsearchRepository releaseElasticsearchRepository;
    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    @Override
    public Mono<Void> save(ReleaseDocument document) {
        return releaseElasticsearchRepository.save(document).then();
    }

    @Override
    public Mono<Void> update(Long releaseId, String title, String description, List<String> mediaUrls) {
        return releaseElasticsearchRepository.findByReleaseId(releaseId)
                .flatMap(doc -> save(ReleaseDocument.builder()
                        .id(doc.getId())
                        .releaseId(doc.getReleaseId())
                        .creatorId(doc.getCreatorId())
                        .title(title != null ? title : doc.getTitle())
                        .description(description != null ? description : doc.getDescription())
                        .level(doc.getLevel())
                        .price(doc.getPrice())
                        .limitedQuantity(doc.getLimitedQuantity())
                        .mediaUrls(mediaUrls != null ? mediaUrls : doc.getMediaUrls())
                        .scheduledAt(doc.getScheduledAt())
                        .createdAt(doc.getCreatedAt())
                        .build()));
    }

    @Override
    public Mono<Void> delete(Long releaseId) {
        return releaseElasticsearchRepository.deleteByReleaseId(releaseId);
    }

    @Override
    public Flux<ReleaseDocument> search(String keyword, int page, int size) {
        return Mono.fromFuture(() -> elasticsearchAsyncClient.search(s -> s
                        .index(INDEX)
                        .from(page * size)
                        .size(size)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(keyword)
                                        .fields("title", "description")
                                        .analyzer("nori")
                                )
                        ),
                ReleaseDocument.class
        )).flatMapMany(response -> Flux.fromIterable(response.hits().hits()).mapNotNull(Hit::source));
    }

    @Override
    public Flux<ReleaseDocument> findPopular(int size) {
        return findSortedByCreatedAt(size);
    }

    @Override
    public Flux<ReleaseDocument> findRecent(int size) {
        return findSortedByCreatedAt(size);
    }

    private Flux<ReleaseDocument> findSortedByCreatedAt(int size) {
        return Mono.fromFuture(() -> elasticsearchAsyncClient.search(s -> s
                        .index(INDEX)
                        .size(size)
                        .sort(sort -> sort.field(f -> f.field("created_at").order(SortOrder.Desc))),
                ReleaseDocument.class
        )).flatMapMany(response -> Flux.fromIterable(response.hits().hits()).mapNotNull(Hit::source));
    }
}

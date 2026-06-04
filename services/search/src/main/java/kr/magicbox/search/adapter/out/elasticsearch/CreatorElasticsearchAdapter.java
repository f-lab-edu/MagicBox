package kr.magicbox.search.adapter.out.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import kr.magicbox.search.adapter.out.elasticsearch.repository.CreatorElasticsearchRepository;
import kr.magicbox.search.application.port.out.CreatorIndexPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CreatorElasticsearchAdapter implements CreatorIndexPort {

    private static final String INDEX = "creator-index";

    private final CreatorElasticsearchRepository creatorElasticsearchRepository;
    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    @Override
    public Mono<CreatorDocument> findByCreatorId(Long creatorId) {
        return creatorElasticsearchRepository.findByCreatorId(creatorId);
    }

    @Override
    public Mono<Void> save(CreatorDocument document) {
        return creatorElasticsearchRepository.save(document).then();
    }

    @Override
    public Mono<Void> update(Long creatorId, String nickname, String tagline, String profileImageUrl) {
        return findByCreatorId(creatorId)
                .flatMap(doc -> save(CreatorDocument.builder()
                        .id(doc.getId())
                        .creatorId(doc.getCreatorId())
                        .nickname(nickname != null ? nickname : doc.getNickname())
                        .tagline(tagline != null ? tagline : doc.getTagline())
                        .profileImageUrl(profileImageUrl != null ? profileImageUrl : doc.getProfileImageUrl())
                        .genres(doc.getGenres())
                        .createdAt(doc.getCreatedAt())
                        .build()));
    }

    @Override
    public Mono<Void> delete(Long creatorId) {
        return creatorElasticsearchRepository.deleteByCreatorId(creatorId);
    }

    @Override
    public Flux<CreatorDocument> search(String keyword, int page, int size) {
        return Mono.fromFuture(() -> elasticsearchAsyncClient.search(s -> s
                        .index(INDEX)
                        .from(page * size)
                        .size(size)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(keyword)
                                        .fields("nickname", "tagline")
                                        .analyzer("nori")
                                )
                        ),
                CreatorDocument.class
        )).flatMapMany(response -> Flux.fromIterable(response.hits().hits()).mapNotNull(Hit::source));
    }

    @Override
    public Flux<CreatorDocument> findPopular(int size) {
        return findSortedByCreatedAt(size);
    }

    @Override
    public Flux<CreatorDocument> findRecent(int size) {
        return findSortedByCreatedAt(size);
    }

    private Flux<CreatorDocument> findSortedByCreatedAt(int size) {
        return Mono.fromFuture(() -> elasticsearchAsyncClient.search(s -> s
                        .index(INDEX)
                        .size(size)
                        .sort(sort -> sort.field(f -> f.field("created_at").order(SortOrder.Desc))),
                CreatorDocument.class
        )).flatMapMany(response -> Flux.fromIterable(response.hits().hits()).mapNotNull(Hit::source));
    }
}

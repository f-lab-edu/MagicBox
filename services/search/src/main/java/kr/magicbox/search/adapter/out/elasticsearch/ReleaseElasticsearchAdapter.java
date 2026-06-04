package kr.magicbox.search.adapter.out.elasticsearch;

import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import kr.magicbox.search.adapter.out.elasticsearch.repository.ReleaseElasticsearchRepository;
import kr.magicbox.search.application.port.out.ReleaseIndexPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReleaseElasticsearchAdapter implements ReleaseIndexPort {

    private final ReleaseElasticsearchRepository releaseElasticsearchRepository;
    private final ReactiveElasticsearchOperations elasticsearchOperations;

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
        Query query = CriteriaQuery.builder(
                new Criteria("title").matches(keyword).or(new Criteria("description").matches(keyword)))
                .withPageable(PageRequest.of(page, size))
                .build();
        return elasticsearchOperations.search(query, ReleaseDocument.class)
                .map(SearchHit::getContent);
    }

    @Override
    public Flux<ReleaseDocument> findPopular(int size) {
        Query query = CriteriaQuery.builder(new Criteria())
                .withPageable(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "likeCount")))
                .build();
        return elasticsearchOperations.search(query, ReleaseDocument.class)
                .map(SearchHit::getContent);
    }

    @Override
    public Flux<ReleaseDocument> findRecent(int size) {
        Query query = CriteriaQuery.builder(new Criteria())
                .withPageable(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt")))
                .build();
        return elasticsearchOperations.search(query, ReleaseDocument.class)
                .map(SearchHit::getContent);
    }
}

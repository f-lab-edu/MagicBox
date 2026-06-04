package kr.magicbox.search.adapter.out.elasticsearch;

import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import kr.magicbox.search.adapter.out.elasticsearch.repository.CreatorElasticsearchRepository;
import kr.magicbox.search.application.port.out.CreatorIndexPort;
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

@Repository
@RequiredArgsConstructor
public class CreatorElasticsearchAdapter implements CreatorIndexPort {

    private final CreatorElasticsearchRepository creatorElasticsearchRepository;
    private final ReactiveElasticsearchOperations elasticsearchOperations;

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
        Criteria criteria = new Criteria("nickname").matches(keyword)
                .or(new Criteria("tagline").matches(keyword));
        Query query = new CriteriaQuery(criteria)
                .setPageable(PageRequest.of(page, size));
        return elasticsearchOperations.search(query, CreatorDocument.class)
                .map(SearchHit::getContent);
    }

    @Override
    public Flux<CreatorDocument> findPopular(int size) {
        Query query = new CriteriaQuery(new Criteria())
                .setPageable(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "followerCount")));
        return elasticsearchOperations.search(query, CreatorDocument.class)
                .map(SearchHit::getContent);
    }

    @Override
    public Flux<CreatorDocument> findRecent(int size) {
        Query query = new CriteriaQuery(new Criteria())
                .setPageable(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return elasticsearchOperations.search(query, CreatorDocument.class)
                .map(SearchHit::getContent);
    }
}

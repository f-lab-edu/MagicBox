package kr.magicbox.search.adapter.out.elasticsearch;

import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import kr.magicbox.search.adapter.out.elasticsearch.repository.GeneralGoodsElasticsearchRepository;
import kr.magicbox.search.application.port.out.GeneralGoodsIndexPort;
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
public class GeneralGoodsElasticsearchAdapter implements GeneralGoodsIndexPort {

    private final GeneralGoodsElasticsearchRepository generalGoodsElasticsearchRepository;
    private final ReactiveElasticsearchOperations elasticsearchOperations;

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
        Criteria criteria = new Criteria("name").matches(keyword);
        Query query = new CriteriaQuery(criteria)
                .setPageable(PageRequest.of(page, size));
        return elasticsearchOperations.search(query, GeneralGoodsDocument.class)
                .map(SearchHit::getContent);
    }

    @Override
    public Flux<GeneralGoodsDocument> findPopular(int size) {
        Query query = new CriteriaQuery(new Criteria())
                .setPageable(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "likeCount")));
        return elasticsearchOperations.search(query, GeneralGoodsDocument.class)
                .map(SearchHit::getContent);
    }

    @Override
    public Flux<GeneralGoodsDocument> findRecent(int size) {
        Query query = new CriteriaQuery(new Criteria())
                .setPageable(PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return elasticsearchOperations.search(query, GeneralGoodsDocument.class)
                .map(SearchHit::getContent);
    }
}

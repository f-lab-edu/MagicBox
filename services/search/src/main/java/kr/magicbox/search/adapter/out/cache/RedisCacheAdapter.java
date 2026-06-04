package kr.magicbox.search.adapter.out.cache;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import kr.magicbox.search.application.dto.result.ReleaseSearchResult;
import kr.magicbox.search.application.port.out.SearchCachePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisCacheAdapter implements SearchCachePort {

    private static final String POPULAR_CREATORS_KEY = "search:popular:creators";
    private static final String POPULAR_RELEASES_KEY = "search:popular:releases";
    private static final String POPULAR_GENERAL_GOODS_KEY = "search:popular:general-goods";
    private static final String RECENT_CREATORS_KEY = "search:recent:creators";
    private static final String RECENT_RELEASES_KEY = "search:recent:releases";
    private static final String RECENT_GENERAL_GOODS_KEY = "search:recent:general-goods";
    private static final String VIEWED_CREATORS_KEY_PREFIX = "search:history:creators:";
    private static final String SEARCH_QUERIES_KEY_PREFIX = "search:history:queries:";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CacheProperties cacheProperties;

    // ===== Cache Aside: 인기 목록 =====

    @Override
    public Mono<List<CreatorSearchResult>> getPopularCreators() {
        return getList(POPULAR_CREATORS_KEY, new TypeReference<>() {});
    }

    @Override
    public Mono<Void> setPopularCreators(List<CreatorSearchResult> creators) {
        return setWithTtl(POPULAR_CREATORS_KEY, creators, cacheProperties.getPopularTtlSeconds());
    }

    @Override
    public Mono<List<ReleaseSearchResult>> getPopularReleases() {
        return getList(POPULAR_RELEASES_KEY, new TypeReference<>() {});
    }

    @Override
    public Mono<Void> setPopularReleases(List<ReleaseSearchResult> releases) {
        return setWithTtl(POPULAR_RELEASES_KEY, releases, cacheProperties.getPopularTtlSeconds());
    }

    @Override
    public Mono<List<GeneralGoodsSearchResult>> getPopularGeneralGoods() {
        return getList(POPULAR_GENERAL_GOODS_KEY, new TypeReference<>() {});
    }

    @Override
    public Mono<Void> setPopularGeneralGoods(List<GeneralGoodsSearchResult> goods) {
        return setWithTtl(POPULAR_GENERAL_GOODS_KEY, goods, cacheProperties.getPopularTtlSeconds());
    }

    // ===== Write Through: 최신 목록 =====

    @Override
    public Mono<Void> addRecentCreator(CreatorDocument document) {
        return addToList(RECENT_CREATORS_KEY, document, cacheProperties.getRecentListSize());
    }

    @Override
    public Flux<CreatorDocument> getRecentCreators() {
        return getListItems(RECENT_CREATORS_KEY, new TypeReference<>() {});
    }

    @Override
    public Mono<Void> addRecentRelease(ReleaseDocument document) {
        return addToList(RECENT_RELEASES_KEY, document, cacheProperties.getRecentListSize());
    }

    @Override
    public Flux<ReleaseDocument> getRecentReleases() {
        return getListItems(RECENT_RELEASES_KEY, new TypeReference<>() {});
    }

    @Override
    public Mono<Void> addRecentGeneralGoods(GeneralGoodsDocument document) {
        return addToList(RECENT_GENERAL_GOODS_KEY, document, cacheProperties.getRecentListSize());
    }

    @Override
    public Flux<GeneralGoodsDocument> getRecentGeneralGoods() {
        return getListItems(RECENT_GENERAL_GOODS_KEY, new TypeReference<>() {});
    }

    // ===== Write Through: 개인화 이력 =====

    @Override
    public Mono<Void> addViewedCreator(Long userId, CreatorDocument document) {
        return addToList(VIEWED_CREATORS_KEY_PREFIX + userId, document, cacheProperties.getHistoryListSize());
    }

    @Override
    public Flux<CreatorDocument> getViewedCreators(Long userId) {
        return getListItems(VIEWED_CREATORS_KEY_PREFIX + userId, new TypeReference<>() {});
    }

    @Override
    public Mono<Void> addSearchQuery(Long userId, String query) {
        String key = SEARCH_QUERIES_KEY_PREFIX + userId;
        return redisTemplate.opsForList().leftPush(key, query)
                .then(redisTemplate.opsForList().trim(key, 0, cacheProperties.getQueryHistorySize() - 1))
                .then()
                .doOnError(e -> log.error("[Cache] 검색어 저장 실패. userId={}, query={}", userId, query, e))
                .onErrorComplete();
    }

    @Override
    public Flux<String> getSearchQueries(Long userId) {
        return redisTemplate.opsForList().range(SEARCH_QUERIES_KEY_PREFIX + userId, 0, -1)
                .doOnError(e -> log.error("[Cache] 검색어 조회 실패. userId={}", userId, e))
                .onErrorResume(e -> Flux.empty());
    }

    // ===== 내부 헬퍼 =====

    private <T> Mono<List<T>> getList(String key, TypeReference<List<T>> typeRef) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(value -> Mono.fromCallable(() -> objectMapper.readValue(value, typeRef)))
                .doOnError(e -> log.error("[Cache] 캐시 조회 실패. key={}", key, e))
                .onErrorResume(e -> Mono.empty());
    }

    private Mono<Void> setWithTtl(String key, Object value, long ttlSeconds) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(value))
                .flatMap(json -> redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttlSeconds)))
                .then()
                .doOnError(e -> log.error("[Cache] 캐시 저장 실패. key={}", key, e))
                .onErrorComplete();
    }

    private <T> Mono<Void> addToList(String key, T item, int maxSize) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(item))
                .flatMap(json -> redisTemplate.opsForList().leftPush(key, json))
                .then(redisTemplate.opsForList().trim(key, 0, maxSize - 1))
                .then()
                .doOnError(e -> log.error("[Cache] 리스트 추가 실패. key={}", key, e))
                .onErrorComplete();
    }

    private <T> Flux<T> getListItems(String key, TypeReference<T> typeRef) {
        return redisTemplate.opsForList().range(key, 0, -1)
                .flatMap(s -> Mono.fromCallable(() -> objectMapper.readValue(s, typeRef)))
                .doOnError(e -> log.error("[Cache] 리스트 조회 실패. key={}", key, e))
                .onErrorResume(e -> Flux.empty());
    }
}

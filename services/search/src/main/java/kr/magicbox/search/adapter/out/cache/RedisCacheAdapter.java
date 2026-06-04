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
        return redisTemplate.opsForValue().get(POPULAR_CREATORS_KEY)
                .flatMap(value -> Mono.fromCallable(() -> objectMapper.readValue(value, new TypeReference<List<CreatorSearchResult>>() {})))
                .doOnError(e -> log.error("[Cache] 인기 크리에이터 조회 실패", e))
                .onErrorResume(e -> Mono.empty());
    }

    @Override
    public Mono<Void> setPopularCreators(List<CreatorSearchResult> creators) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(creators))
                .flatMap(json -> redisTemplate.opsForValue().set(POPULAR_CREATORS_KEY, json, Duration.ofSeconds(cacheProperties.getPopularTtlSeconds())))
                .then()
                .doOnError(e -> log.error("[Cache] 인기 크리에이터 저장 실패", e))
                .onErrorComplete();
    }

    @Override
    public Mono<List<ReleaseSearchResult>> getPopularReleases() {
        return redisTemplate.opsForValue().get(POPULAR_RELEASES_KEY)
                .flatMap(value -> Mono.fromCallable(() -> objectMapper.readValue(value, new TypeReference<List<ReleaseSearchResult>>() {})))
                .doOnError(e -> log.error("[Cache] 인기 릴리즈 조회 실패", e))
                .onErrorResume(e -> Mono.empty());
    }

    @Override
    public Mono<Void> setPopularReleases(List<ReleaseSearchResult> releases) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(releases))
                .flatMap(json -> redisTemplate.opsForValue().set(POPULAR_RELEASES_KEY, json, Duration.ofSeconds(cacheProperties.getPopularTtlSeconds())))
                .then()
                .doOnError(e -> log.error("[Cache] 인기 릴리즈 저장 실패", e))
                .onErrorComplete();
    }

    @Override
    public Mono<List<GeneralGoodsSearchResult>> getPopularGeneralGoods() {
        return redisTemplate.opsForValue().get(POPULAR_GENERAL_GOODS_KEY)
                .flatMap(value -> Mono.fromCallable(() -> objectMapper.readValue(value, new TypeReference<List<GeneralGoodsSearchResult>>() {})))
                .doOnError(e -> log.error("[Cache] 인기 일반상품 조회 실패", e))
                .onErrorResume(e -> Mono.empty());
    }

    @Override
    public Mono<Void> setPopularGeneralGoods(List<GeneralGoodsSearchResult> goods) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(goods))
                .flatMap(json -> redisTemplate.opsForValue().set(POPULAR_GENERAL_GOODS_KEY, json, Duration.ofSeconds(cacheProperties.getPopularTtlSeconds())))
                .then()
                .doOnError(e -> log.error("[Cache] 인기 일반상품 저장 실패", e))
                .onErrorComplete();
    }

    // ===== Write Through: 최신 목록 =====

    @Override
    public Mono<Void> addRecentCreator(CreatorDocument document) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(document))
                .flatMap(json -> redisTemplate.opsForList().leftPush(RECENT_CREATORS_KEY, json))
                .then(redisTemplate.opsForList().trim(RECENT_CREATORS_KEY, 0, cacheProperties.getRecentListSize() - 1))
                .then()
                .doOnError(e -> log.error("[Cache] 최신 크리에이터 추가 실패", e))
                .onErrorComplete();
    }

    @Override
    public Flux<CreatorDocument> getRecentCreators() {
        return redisTemplate.opsForList().range(RECENT_CREATORS_KEY, 0, -1)
                .flatMap(s -> Mono.fromCallable(() -> objectMapper.readValue(s, CreatorDocument.class)))
                .doOnError(e -> log.error("[Cache] 최신 크리에이터 조회 실패", e))
                .onErrorResume(e -> Flux.empty());
    }

    @Override
    public Mono<Void> addRecentRelease(ReleaseDocument document) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(document))
                .flatMap(json -> redisTemplate.opsForList().leftPush(RECENT_RELEASES_KEY, json))
                .then(redisTemplate.opsForList().trim(RECENT_RELEASES_KEY, 0, cacheProperties.getRecentListSize() - 1))
                .then()
                .doOnError(e -> log.error("[Cache] 최신 릴리즈 추가 실패", e))
                .onErrorComplete();
    }

    @Override
    public Flux<ReleaseDocument> getRecentReleases() {
        return redisTemplate.opsForList().range(RECENT_RELEASES_KEY, 0, -1)
                .flatMap(s -> Mono.fromCallable(() -> objectMapper.readValue(s, ReleaseDocument.class)))
                .doOnError(e -> log.error("[Cache] 최신 릴리즈 조회 실패", e))
                .onErrorResume(e -> Flux.empty());
    }

    @Override
    public Mono<Void> addRecentGeneralGoods(GeneralGoodsDocument document) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(document))
                .flatMap(json -> redisTemplate.opsForList().leftPush(RECENT_GENERAL_GOODS_KEY, json))
                .then(redisTemplate.opsForList().trim(RECENT_GENERAL_GOODS_KEY, 0, cacheProperties.getRecentListSize() - 1))
                .then()
                .doOnError(e -> log.error("[Cache] 최신 일반상품 추가 실패", e))
                .onErrorComplete();
    }

    @Override
    public Flux<GeneralGoodsDocument> getRecentGeneralGoods() {
        return redisTemplate.opsForList().range(RECENT_GENERAL_GOODS_KEY, 0, -1)
                .flatMap(s -> Mono.fromCallable(() -> objectMapper.readValue(s, GeneralGoodsDocument.class)))
                .doOnError(e -> log.error("[Cache] 최신 일반상품 조회 실패", e))
                .onErrorResume(e -> Flux.empty());
    }

    // ===== Write Through: 개인화 이력 =====

    @Override
    public Mono<Void> addViewedCreator(Long userId, CreatorDocument document) {
        String key = VIEWED_CREATORS_KEY_PREFIX + userId;
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(document))
                .flatMap(json -> redisTemplate.opsForList().leftPush(key, json))
                .then(redisTemplate.opsForList().trim(key, 0, cacheProperties.getHistoryListSize() - 1))
                .then()
                .doOnError(e -> log.error("[Cache] 조회 크리에이터 추가 실패. userId={}", userId, e))
                .onErrorComplete();
    }

    @Override
    public Flux<CreatorDocument> getViewedCreators(Long userId) {
        return redisTemplate.opsForList().range(VIEWED_CREATORS_KEY_PREFIX + userId, 0, -1)
                .flatMap(s -> Mono.fromCallable(() -> objectMapper.readValue(s, CreatorDocument.class)))
                .doOnError(e -> log.error("[Cache] 조회 크리에이터 조회 실패. userId={}", userId, e))
                .onErrorResume(e -> Flux.empty());
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
}

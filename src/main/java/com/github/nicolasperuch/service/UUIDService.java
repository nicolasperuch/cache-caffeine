package com.github.nicolasperuch.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Service
public class UUIDService {

	private final Cache cache;

	public UUIDService(Cache cache) {
		this.cache = cache;
	}

	public Mono<String> generateUUID(String key) {
		return Mono
				.just(key)
				.filter(k -> cache.getIfPresent(k) == null)
				.map(this::generateUUIDFromKey)
				.map(v -> cacheIt(key, v))
				.switchIfEmpty(getFromCache(key));
	}

	private String generateUUIDFromKey(String value) {
		return value.concat(UUID.randomUUID().toString());
	}

	private String cacheIt(String key, String value) {
		cache.put(key, value);
		return value;
	}

	private Mono<String> getFromCache(String key) {
		return Mono
				.just(key)
				.map((Function<String, Object>) cache::getIfPresent)
				.map(v -> (String) v);
	}
}

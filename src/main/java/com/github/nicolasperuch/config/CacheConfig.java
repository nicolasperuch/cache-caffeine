package com.github.nicolasperuch.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

	@Bean
	public Cache cache() {
		return Caffeine.newBuilder()
				.expireAfter(cacheCustomConfigs())
				.build();
	}

	private Expiry cacheCustomConfigs() {
		return new Expiry<Object, Object>() {
			@Override
			public long expireAfterCreate(@NonNull Object key, @NonNull Object value, long currentTime) {
				return expiryConfig();
			}

			@Override
			public long expireAfterUpdate(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
				return TimeUnit.MINUTES.toNanos(5);
			}

			@Override
			public long expireAfterRead(@NonNull Object key, @NonNull Object value, long currentTime, @NonNegative long currentDuration) {
				return expiryConfig();
			}
		};
	}

	private long expiryConfig() {
		var midDayThreshold = LocalDate.now().atTime(17, 43);
		var now = LocalDateTime.now();
		LocalDateTime tempMidDayDateTime = LocalDateTime.from( now );
		long minutesUntilExpireMidDay = tempMidDayDateTime.until(midDayThreshold, ChronoUnit.MINUTES);

		var midNightThreshold = LocalDate.now().atTime(17,46);
		LocalDateTime tempMidNightDateTime = LocalDateTime.from( now );
		long minutesUntilExpireMidNight = tempMidNightDateTime.until(midNightThreshold, ChronoUnit.MINUTES);

		if(minutesUntilExpireMidDay > 0) {
			return TimeUnit.MINUTES.toNanos(minutesUntilExpireMidDay);
		} else {
			return TimeUnit.MINUTES.toNanos(minutesUntilExpireMidNight);
		}
	}
}

package com.example.student.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching  // Enables caching in Spring Boot
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("students_cache", "student_cache");
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // Cache entries expire after 10 minutes
                .maximumSize(100) // Max 100 entries in cache
                .recordStats(); // Enables cache statistics
    }
}

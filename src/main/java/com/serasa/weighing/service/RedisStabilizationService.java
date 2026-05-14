package com.serasa.weighing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do motor de estabilização usando Redis.
 *
 * Estratégia: Sliding Window + Desvio Padrão.
 * - Acumula leituras numa Redis List (chave por balança+placa)
 * - Quando a janela atinge o tamanho mínimo, calcula o desvio padrão
 * - Se o desvio < threshold, o peso está estabilizado (retorna a média)
 * - TTL no Redis garante limpeza automática de buffers órfãos
 */
@Service
@RequiredArgsConstructor
public class RedisStabilizationService implements StabilizationService {

    private final StringRedisTemplate redisTemplate;

    @Value("${stabilization.window-size}")
    private int windowSize;

    @Value("${stabilization.std-dev-threshold}")
    private double stdDevThreshold;

    @Value("${stabilization.buffer-ttl-seconds}")
    private long bufferTtlSeconds;

    @Override
    public Optional<Double> addReadingAndCheck(String scaleId, String plate, double weight) {
        String key = buildKey(scaleId, plate);

        pushReading(key, weight);

        return getWindow(key)
                .filter(this::isStable)
                .map(this::calculateMean)
                .map(mean -> {
                    redisTemplate.delete(key);
                    return mean;
                });
    }

    private String buildKey(String scaleId, String plate) {
        return "scale:" + scaleId + ":plate:" + plate;
    }

    private void pushReading(String key, double weight) {
        redisTemplate.opsForList().rightPush(key, String.valueOf(weight));
        redisTemplate.expire(key, Duration.ofSeconds(bufferTtlSeconds));
    }

    private Optional<List<Double>> getWindow(String key) {
        Long size = redisTemplate.opsForList().size(key);
        if (size == null || size < windowSize) {
            return Optional.empty();
        }

        List<String> rawValues = redisTemplate.opsForList().range(key, -windowSize, -1);
        if (rawValues == null || rawValues.size() < windowSize) {
            return Optional.empty();
        }

        return Optional.of(rawValues.stream().map(Double::parseDouble).toList());
    }

    private boolean isStable(List<Double> window) {
        double mean = calculateMean(window);
        double variance = window.stream()
                .mapToDouble(d -> Math.pow(d - mean, 2))
                .average().orElse(0);
        return Math.sqrt(variance) < stdDevThreshold;
    }

    private double calculateMean(List<Double> window) {
        return window.stream().mapToDouble(d -> d).average().orElse(0);
    }
}

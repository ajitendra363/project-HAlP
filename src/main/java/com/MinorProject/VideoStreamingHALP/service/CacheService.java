package com.MinorProject.VideoStreamingHALP.service;

import com.MinorProject.VideoStreamingHALP.model.CacheEntry;
import com.MinorProject.VideoStreamingHALP.model.Video;
import com.MinorProject.VideoStreamingHALP.repository.CacheRepository;
import com.MinorProject.VideoStreamingHALP.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheRepository cacheRepository;
    private final VideoRepository videoRepository;
    private final MLService mlService;

    // Configurable capacity
    private long capacityBytes = 1_000_000_000L;
    private int candidateK = 4;

    public void setCapacityBytes(long cap) { this.capacityBytes = cap; }
    public void setCandidateK(int k) { this.candidateK = Math.max(1, k); }

    // Touch on access to update metadata
    public void touch(Long videoId, Long sizeBytes) {
        CacheEntry e = cacheRepository.findByVideoId(videoId).orElseGet(() ->
                CacheEntry.builder()
                        .videoId(videoId)
                        .hits(0L)
                        .sizeBytes(sizeBytes)
                        .lastAccessed(Instant.now())
                        .score(0.0)
                        .build()
        );
        e.setHits((e.getHits() == null ? 0L : e.getHits()) + 1);
        e.setLastAccessed(Instant.now());
        cacheRepository.save(e);
    }

    // ✅ Get cache entry by videoId
    public CacheEntry getCacheEntry(Long videoId) {
        return cacheRepository.findByVideoId(videoId).orElse(null);
    }

    // List all cache entries
    public List<CacheEntry> list() {
        return cacheRepository.findAll();
    }

    // Select candidates for eviction
    private List<CacheEntry> selectCandidates() {
        List<CacheEntry> all = cacheRepository.findAll();
        return all.stream()
                .sorted(Comparator
                        .comparingLong((CacheEntry e) -> e.getHits() == null ? 0L : e.getHits())
                        .thenComparing(e -> e.getLastAccessed() == null ? Instant.EPOCH : e.getLastAccessed()))
                .limit(candidateK)
                .collect(Collectors.toList());
    }

    // Re-rank candidates using ML and pick one to evict
    private Optional<CacheEntry> rerankAndPickEviction(List<CacheEntry> candidates) {
        if (candidates.isEmpty()) return Optional.empty();

        Map<Long, Video> videos = videoRepository.findAllById(
                candidates.stream().map(CacheEntry::getVideoId).toList()
        ).stream().collect(Collectors.toMap(Video::getId, v -> v));

        CacheEntry best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (CacheEntry c : candidates) {
            Video v = videos.get(c.getVideoId());
            long size = c.getSizeBytes() == null ? 0L : c.getSizeBytes();
            long hits = c.getHits() == null ? 0L : c.getHits();
            double s = mlService.score(v, 0L, hits, size);
            c.setScore(s);
            if (s > bestScore) { bestScore = s; best = c; }
        }

        if (best != null) cacheRepository.saveAll(candidates);
        return Optional.ofNullable(best);
    }

    // Ensure total cache size <= capacity, evict if necessary
    public void ensureCapacity() {
        long total = cacheRepository.findAll().stream()
                .mapToLong(e -> e.getSizeBytes() == null ? 0L : e.getSizeBytes())
                .sum();
        if (total <= capacityBytes) return;

        List<CacheEntry> candidates = selectCandidates();
        Optional<CacheEntry> toEvict = rerankAndPickEviction(candidates);

        toEvict.ifPresent(cacheRepository::delete);
    }
}

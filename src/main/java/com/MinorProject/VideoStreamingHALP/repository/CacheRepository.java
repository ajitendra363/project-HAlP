package com.MinorProject.VideoStreamingHALP.repository;

import com.MinorProject.VideoStreamingHALP.model.CacheEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CacheRepository extends JpaRepository<CacheEntry, Long> {
    Optional<CacheEntry> findByVideoId(Long videoId);
}

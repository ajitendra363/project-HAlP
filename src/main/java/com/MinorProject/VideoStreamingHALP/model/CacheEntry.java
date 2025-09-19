package com.MinorProject.VideoStreamingHALP.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "cache_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long videoId;

    private Long sizeBytes;
    private Long hits;
    private Instant lastAccessed;
    private Double score; // HALP score snapshot
}

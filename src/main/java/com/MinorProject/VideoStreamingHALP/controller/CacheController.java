package com.MinorProject.VideoStreamingHALP.controller;

import com.MinorProject.VideoStreamingHALP.model.CacheEntry;
import com.MinorProject.VideoStreamingHALP.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cache")
public class CacheController {
    private final CacheService cacheService;

    // ✅ List all entries
    @GetMapping("/entries")
    public List<CacheEntry> entries() {
        return cacheService.list();
    }

    // ✅ Insert or update a cache entry (this is what was missing earlier)
    @PostMapping("/touch/{videoId}/{sizeBytes}")
    public String touch(@PathVariable Long videoId, @PathVariable Long sizeBytes) {
        cacheService.touch(videoId, sizeBytes);
        return "Cache entry updated for videoId=" + videoId;
    }

    // ✅ Ensure capacity
    @PostMapping("/ensureCapacity")
    public String ensure() {
        cacheService.ensureCapacity();
        return "OK";
    }

    // ✅ Set candidate K
    @PostMapping("/candidateK/{k}")
    public String setK(@PathVariable int k) {
        cacheService.setCandidateK(k);
        return "OK";
    }

    // ✅ Set capacity
    @PostMapping("/capacity/{bytes}")
    public String setCap(@PathVariable long bytes) {
        cacheService.setCapacityBytes(bytes);
        return "OK";
    }
}

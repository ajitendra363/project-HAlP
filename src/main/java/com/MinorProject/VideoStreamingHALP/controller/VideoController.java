package com.MinorProject.VideoStreamingHALP.controller;

import com.MinorProject.VideoStreamingHALP.model.Video;
import com.MinorProject.VideoStreamingHALP.model.CacheEntry;
import com.MinorProject.VideoStreamingHALP.service.VideoService;
import com.MinorProject.VideoStreamingHALP.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CacheService cacheService;

    // Home page
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("videos", videoService.getAllVideos());
        return "index";
    }

    // Upload page
    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    // Handle upload
    @PostMapping("/upload")
    public String uploadVideo(@RequestParam("file") MultipartFile file,
                              @RequestParam("title") String title,
                              @RequestParam("description") String description) throws IOException {

        Video savedVideo = videoService.uploadVideo(file, title, description);

        // Update cache on upload
        cacheService.touch(savedVideo.getId(), savedVideo.getSizeBytes());

        return "redirect:/";
    }

    // Play video
    @GetMapping("/video/{id}")
    public String playVideo(@PathVariable Long id, Model model) {
        Video video = videoService.getVideo(id);

        // Update cache on play
        cacheService.touch(video.getId(), video.getSizeBytes());

        // Fetch cache entry separately
        CacheEntry entry = cacheService.getCacheEntry(video.getId());

        model.addAttribute("video", video);
        model.addAttribute("cacheEntry", entry);

        return "player";
    }
}

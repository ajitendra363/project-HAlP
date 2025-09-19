package com.MinorProject.VideoStreamingHALP.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String filePath;  // Location of stored video

    private Long sizeBytes;   // NEW: file size in bytes
}

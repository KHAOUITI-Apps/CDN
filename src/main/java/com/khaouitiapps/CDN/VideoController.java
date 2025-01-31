/**
 * Copyright © KHAOUITI Apps 2025 | https://www.khaouitiapps.com/
 *
 * Author: KHAOUITI ABDELHAKIM (Software Engineer from ENSIAS)
 *
 * Any use, distribution, or modification of this code must be explicitly allowed by the owner.
 * For permissions, contact me or visit my LinkedIn:
 * https://www.linkedin.com/in/khaouitiabdelhakim/
 */

package com.khaouitiapps.CDN;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.khaouitiapps.CDN.URLSigner.validateSignedUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {


    private static final String VIDEO_DIRECTORY = "videos"; // Directory containing video files

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // Check if behind a proxy
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP"); // Another common header
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // Fallback to standard method
        }

        // If multiple IPs are in X-Forwarded-For, take the first one (real client IP)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }


    @GetMapping("/all")
    public List<Map<String, String>> getAllVideos(HttpServletRequest request) {
        List<Map<String, String>> videos = new ArrayList<>();
        File directory = new File(VIDEO_DIRECTORY);

        String clientIp = getClientIp(request); // Get correct IP
        System.out.println("Client IP: " + clientIp);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".mp4"));
            if (files != null) {
                for (File file : files) {
                    try {
                        String videoId = file.getName().replace(".mp4", "");
                        String signedUrl = URLSigner.generateSignedUrl(videoId, clientIp);

                        Map<String, String> videoInfo = new HashMap<>();
                        videoInfo.put("videoId", videoId);
                        videoInfo.put("signedUrl", signedUrl);

                        videos.add(videoInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return videos;
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<?> streamVideo(@PathVariable String videoId,
                                         @RequestParam String token,
                                         @RequestParam long expires,
                                         @RequestParam String ip,
                                         HttpServletRequest request) throws Exception {
        System.out.println("Incoming request to stream: " + videoId);
        System.out.println("Token: " + token);
        System.out.println("Expires: " + expires);

        String clientIp = request.getRemoteAddr();
        System.out.println("Extracted Client IP: " + clientIp);

        // Validate IP-based Token
        if (!validateSignedUrl(videoId, token, expires, clientIp)) {
            System.out.println("Invalid or expired token! Rejecting request.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Invalid or expired token. Access denied."));
        }

        Path videoPath = Paths.get("videos/" + videoId + ".mp4");

        // Check if the video file exists
        if (!Files.exists(videoPath)) {
            System.out.println("File not found: " + videoPath.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Requested video not found."));
        }

        // Serve the video file
        Resource resource = new UrlResource(videoPath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .body(resource);
    }
}

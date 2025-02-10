package com.mediascout.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mediascout.service.MediaService;

@Controller
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/")
    public String showLandingPage() {
        return "index";
    }

    @PostMapping("/getResult")
    @ResponseBody
    public Map<String, String> getMediaSuggestions(@RequestBody Map<String, String> request) {
        String timePeriod = request.get("timePeriod");
        String mediaType = request.get("mediaType");
        String additionalInfo = request.get("additionalInfo");

        String mediaTitle = mediaService.getMediaSuggestion(mediaType, timePeriod, additionalInfo);
        if (mediaTitle == null || mediaTitle.isEmpty()) {
            return Map.of("error", "No media suggestion received. Please try again.");
        }

        String imageUrl = mediaService.fetchImageFromGoogle(mediaTitle);

        Map<String, String> result = new HashMap<>();
        result.put("title", mediaTitle);
        result.put("imageUrl", imageUrl != null ? imageUrl : "");
        return result;
    }
}

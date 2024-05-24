package com.kakao.tech.springaibasic;

import org.springframework.ai.image.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImageController {

    private final ImageModel imageModel;

    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/image")
    public String imageGen(@RequestParam String request) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .withModel("dall-e-3")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(request, options);
        ImageResponse response = imageModel.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();

        return "redirect:" + imageUrl;
    }
}
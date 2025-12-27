package com.cigi.pickthem.controllers;


import com.cigi.pickthem.services.impl.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@CrossOrigin
public class UploadController {

    private final CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file
    ) {
        String imageUrl = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteImage(@PathVariable String publicId) {
        try {
            boolean deleted = cloudinaryService.deleteImage(publicId);
            if (deleted) {
                return ResponseEntity.ok("Image deleted successfully.");
            } else {
                return ResponseEntity.badRequest().body("Failed to delete image.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

package com.cigi.pickthem.controllers;


import com.cigi.pickthem.domain.dtos.CloudinaryResponse;
import com.cigi.pickthem.exception.UnauthorizedException;
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

    @PostMapping("/user/{userId}")
    public ResponseEntity<CloudinaryResponse> uploadUserImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) {
        // Upload image via Cloudinary
        CloudinaryResponse response = cloudinaryService.uploadImage(file);

        // Ici, tu peux mettre à jour directement le user si tu veux
        // userService.updateUserImage(userId, response.getUrl(), response.getPublicId());

        CloudinaryResponse uploadResponse = new CloudinaryResponse(response.getUrl(), response.getPublicId());
        return ResponseEntity.ok(uploadResponse);
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

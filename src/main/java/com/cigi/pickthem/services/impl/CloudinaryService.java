package com.cigi.pickthem.services.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "pick_them"
                    )
            );
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public boolean deleteImage(String publicId) throws Exception {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        // Cloudinary returns {"result":"ok"} if deleted
        return "ok".equals(result.get("result"));
    }
}
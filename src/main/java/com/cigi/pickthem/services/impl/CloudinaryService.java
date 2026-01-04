package com.cigi.pickthem.services.impl;


import com.cigi.pickthem.domain.dtos.CloudinaryResponse;
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

    // Retourne URL + publicId
    public CloudinaryResponse uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "pick_them"
                    )
            );

            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString(); // <-- publicId récupéré

            return new CloudinaryResponse(url, publicId);

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    public boolean deleteImage(String publicId) throws Exception {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"));
    }
}

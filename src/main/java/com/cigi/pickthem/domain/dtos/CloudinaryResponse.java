package com.cigi.pickthem.domain.dtos;

import lombok.Getter;

@Getter
public class CloudinaryResponse {

    private final String url;
    private final String publicId;

    public CloudinaryResponse(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }

}

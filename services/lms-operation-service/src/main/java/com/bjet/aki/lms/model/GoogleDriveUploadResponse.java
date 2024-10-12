package com.bjet.aki.lms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleDriveUploadResponse {
    private int status;
    private String message;
    private String url;
}

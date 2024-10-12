package com.bjet.aki.lms.service;

import com.bjet.aki.lms.asset.ListResultBuilder;
import com.bjet.aki.lms.asset.PagedResult;
import com.bjet.aki.lms.asset.PagedResultBuilder;
import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.jpa.FileManagementEntity;
import com.bjet.aki.lms.mapper.FileManagementMapper;
import com.bjet.aki.lms.model.FileManagement;
import com.bjet.aki.lms.model.FileType;
import com.bjet.aki.lms.model.GoogleDriveUploadResponse;
import com.bjet.aki.lms.repository.FileManagementRepository;
import com.bjet.aki.lms.specification.FileManagementSpecification;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FileManagementService {

    private final FileManagementRepository fileManagementRepository;
    private final FileManagementMapper fileManagementMapper;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_PATH = getPathToGoogleCredentials();

    private static String getPathToGoogleCredentials() {
        String currentDir = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDir, "services", "lms-operation-service", "aki-lms-33019b2f2194.json");
        return filePath.toString();
    }

    public GoogleDriveUploadResponse uploadFilesToGoogleDrive(MultipartFile file, FileType fileType) {
        log.info("Uploading file to google drive. File name: {} and type: {}", file.getName(), fileType.name());
        GoogleDriveUploadResponse response = new GoogleDriveUploadResponse();
        try {
            Drive drive = createDriveService();
            String EXCEL_FOLDER_ID = "17F7AxGod_R_onZmg6KiqN1ZbQS2Gk36L";
            String DOCUMENT_FOLDER_ID = "12tW9Mj54yUx3ishrDygdJ9CV_T4rcNaJ";
            String folderId = fileType.equals(FileType.EXCEL) ? EXCEL_FOLDER_ID : DOCUMENT_FOLDER_ID;
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getOriginalFilename());
            fileMetaData.setParents(Collections.singletonList(folderId));
            var uploadedFile = drive.files().create(fileMetaData, new InputStreamContent(
                            file.getContentType(),
                            new ByteArrayInputStream(file.getBytes()))
                    )
                    .setFields("id").execute();
            String fileUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
            response.setStatus(200);
            response.setMessage("File Successfully Uploaded To Drive");
            response.setUrl(fileUrl);
        } catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            response.setStatus(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential credentials = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentials).build();
    }

    public PagedResult<FileManagement> findAllFiles(Pageable pageable, Long courseId, Long moduleId) {
        Page<FileManagementEntity> files = fileManagementRepository.findAll(FileManagementSpecification.findFiles(courseId, moduleId), pageable);
        return PagedResultBuilder.build(files, fileManagementMapper.toDomain());
    }

    public List<FileManagement> findAllFiles(Long courseId, Long moduleId) {
        List<FileManagementEntity> files = fileManagementRepository.findAll(FileManagementSpecification.findFiles(courseId, moduleId));
        return ListResultBuilder.build(files, fileManagementMapper.toDomain());
    }

    public void uploadFile(Long courseId, Long moduleId, MultipartFile file) {
        FileType fileType = getFileType(file);
        if(fileType == null) {
            throw new CommonException("07", "Valid type not supported");
        }
        GoogleDriveUploadResponse response = this.uploadFilesToGoogleDrive(file, fileType);
        if(response.getStatus() == 200){
            FileManagementEntity entity = new FileManagementEntity();
            entity.setName(file.getOriginalFilename());
            entity.setType(fileType);
            entity.setCourseId(courseId);
            entity.setModuleId(moduleId);
            entity.setUrl(response.getUrl());
            fileManagementRepository.save(entity);
        } else {
            throw new CommonException("08", "Exception occurred in file upload");
        }
    }

    private FileType getFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return switch (extension) {
            case "xlsx" -> FileType.EXCEL;
            case "docs" -> FileType.DOCS;
            case "pptx" -> FileType.PPT;
            case "pdf" -> FileType.PDF;
            default -> null;
        };
    }
}

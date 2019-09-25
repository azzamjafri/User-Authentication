package com.gpch.login.controller;


import com.gpch.login.model.FileUpload;
import com.gpch.login.payload.FileUploadResponse;
import com.gpch.login.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public FileUploadResponse uploadFile(@RequestParam("file")MultipartFile file) {

        FileUpload fileUpload = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileUpload.getId())
                .toUriString();

        return new FileUploadResponse(fileUpload.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());

    }


    @PostMapping("/uploadMultipleFiles")
    public List<FileUploadResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        //FileStorageService fileStorageService;
        FileUpload dbFile = fileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }


}

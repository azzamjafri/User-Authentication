package com.gpch.login.service;


//import antlr.StringUtils;
import com.gpch.login.exception.MyFileNotFoundException;
import com.gpch.login.model.FileUpload;
import com.gpch.login.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    public FileUpload storeFile(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        FileUpload fileUpload = null;
        try {
            fileUpload = new FileUpload(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUploadRepository.save(fileUpload);
    }


    public FileUpload getFile(String fileId){
        return fileUploadRepository.findById(fileId)
                .orElseThrow(()-> new MyFileNotFoundException("File not found with Id " + fileId));
    }



}

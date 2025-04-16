package com.example.Sekai_Academy.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private static final String BASE_PATH = "C:/Users/ASUS/Desktop/AAD Coursework/Backend/src/main/resources/static/";

    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(BASE_PATH + uploadDir);

        // Ensure the directory exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String imgUrl = "";

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            // Generate the image URL
            imgUrl = "/" + uploadDir + "/" + fileName;
        } catch (IOException ioException) {
            System.out.printf("Error saving file: %s\n", ioException.getMessage());
            throw ioException;
        }

        return imgUrl;
    }
}
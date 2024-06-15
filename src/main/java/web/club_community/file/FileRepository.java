package web.club_community.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// 파일 저장용
@Component
public class FileRepository {
    public void save(FileProperty fileProperty, MultipartFile multipartFile) {
        try {
            File fsFile = new File(fileProperty.getFilePath());
            multipartFile.transferTo(fsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(FileProperty fileProperty) {
        try {
            File fsFile = new File(fileProperty.getFilePath());
            Files.deleteIfExists(fsFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


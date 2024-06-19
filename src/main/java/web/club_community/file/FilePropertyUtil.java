package web.club_community.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component // Spring Bean으로 관리되는 경우 Component를 사용하는데 @Value도 이에 포함.
public class FilePropertyUtil {
    @Value("${file.root-path}")
    private String FILE_ROOT_PATH;
    @Value("${file.root-url}")
    private String FILE_ROOT_URL;

    private static String createStoredFileName(String uploadedFileName) {
        String ext = uploadedFileName.substring(uploadedFileName.lastIndexOf('.'));
        return UUID.randomUUID().toString().replace("-", "").concat(ext);
    }

    public FileProperty createFileProperty(MultipartFile multipartFile) {
        String uploadedFileName = multipartFile.getOriginalFilename();
        String storedFileName = createStoredFileName(uploadedFileName);
        FileProperty fileProperty = FileProperty.builder()
                .uploadFileName(uploadedFileName)
                .storedFileName(storedFileName)
                .filePath(createFilePath(storedFileName))
                .fileUrl(createFileUrl(storedFileName))
                .fileSize(multipartFile.getSize())
                .contentType(multipartFile.getContentType())
                .uploadTime(LocalDateTime.now())
                .build();
        System.out.println("createdFileProperty");
        return fileProperty;
    }

    private String createFilePath(String storedFileName) {
        return String.format("%s/%s", FILE_ROOT_PATH, storedFileName);
    }

    private String createFileUrl(String storedFileName) {
        return String.format("%s/%s", FILE_ROOT_URL, storedFileName);
    }
}
package web.club_community.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FileProperty {
    private String uploadFileName; // 사용자가 업로드한 파일이름
    private String storedFileName; // 실제 FS에 저장되는 파일이름
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String contentType;
    private LocalDateTime uploadTime;
}

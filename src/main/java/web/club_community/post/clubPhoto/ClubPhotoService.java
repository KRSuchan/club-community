package web.club_community.post.clubPhoto;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.club_community.club.ClubRepository;
import web.club_community.domain.Club;
import web.club_community.domain.ClubPhoto;
import web.club_community.domain.Member;
import web.club_community.file.FileProperty;
import web.club_community.file.FilePropertyUtil;
import web.club_community.file.FileRepository;
import web.club_community.post.clubPhoto.dto.PhotoCreateRequest;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class ClubPhotoService {
    private final ClubPhotoRepository clubPhotoRepository;
    private final FilePropertyUtil filePropertyUtil;
    private final FileRepository fileRepository;
    private final ClubRepository clubRepository;

    public Slice<ClubPhoto> findPhotosMainPage(Pageable pageable) {
        return clubPhotoRepository.findAllSliceOrderByCreatedTime(pageable);
    }

    public Page<ClubPhoto> findPhotosPageable(Pageable pageable) {
        return clubPhotoRepository.findAllPageOrderByCreatedTime(pageable);
    }

    public ClubPhoto findById(Integer id) {
        return clubPhotoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("조회된 사진 게시글이 없습니다."));
    }

    public ClubPhoto createPhoto(PhotoCreateRequest photoCreateRequest, Member member, MultipartFile photoFile) {
        Club club = clubRepository.findByMaster(member)
                .orElseThrow(() -> new NoSuchElementException("동아리장인 동아리가 조회되지 않았습니다."));
        FileProperty fileProperty = filePropertyUtil.createFileProperty(photoFile);
        ClubPhoto clubPhoto = ClubPhoto.builder()
                .title(photoCreateRequest.getTitle())
                .fileName(fileProperty.getUploadFileName())
                .filePath(fileProperty.getFilePath())
                .build();
        // 파일 저장
        fileRepository.save(fileProperty, photoFile);
        return clubPhotoRepository.save(clubPhoto);
    }
}

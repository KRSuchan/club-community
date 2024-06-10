package web.club_community.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor //역직렬화를 위한 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {

    //회원 번호
    @JsonProperty("id")
    public Long id;
    
    //서비스에 연결 완료된 시각. UTC
    @JsonProperty("connected_at")
    public Date connectedAt;

    //카카오 계정 정보
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        //카카오계정 이름
        @JsonProperty("name")
        public String name;

        //카카오계정 대표 이메일
        @JsonProperty("email")
        public String email;

        //출생 연도 (YYYY 형식)
        @JsonProperty("birthyear")
        public String birthYear;

        //생일 (MMDD 형식)
        @JsonProperty("birthday")
        public String birthDay;

        //생일 타입
        // SOLAR(양력) 혹은 LUNAR(음력)
        @JsonProperty("birthday_type")
        public String birthDayType;

        //성별
        @JsonProperty("gender")
        public String gender;

        //전화번호
        //국내 번호인 경우 +82 00-0000-0000 형식
        @JsonProperty("phone_number")
        public String phoneNumber;
    }

}
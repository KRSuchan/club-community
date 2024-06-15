package web.club_community.Member;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.club_community.Member.dto.request.MemberLoginRequest;
import web.club_community.Member.dto.request.MemberSignupRequest;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.DuplicateEmailException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username)
                .map(this::createUserDetails)
                .orElseThrow(() ->
                        new UsernameNotFoundException("입력된 정보가 잘못 되었습니다.")
                );
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(String.valueOf(member.getRoles()))
                .build();
    }

    public Member signupMember(MemberSignupRequest request) {
        if (memberRepository.findById(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 이메일 입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .department(request.getDepartment())
                .code(request.getCode())
                .phoneNumber(request.getPhoneNumber())
                .roles(List.of("member"))
                .build();
        return memberRepository.save(member);
    }

    public Member loginMember(MemberLoginRequest request) {
        Member member = memberRepository.findById(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("입력된 정보가 잘못 되었습니다."));
        if (!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("입력된 정보가 잘못 되었습니다.");
        }
        return member;
    }
}

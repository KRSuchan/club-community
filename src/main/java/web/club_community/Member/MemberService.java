package web.club_community.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.club_community.domain.Member;
import web.club_community.exception.runtime.DuplicateEmailException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username)
                .map(this::createUserDetails)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username + ": 해당하는 회원이 조회되지 않습니다.")
                );
    }

    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    public Member signupMember(MemberCreateRequest request) {
        if (memberRepository.findById(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("이미 존재하는 이메일 입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .department(request.getDepartment())
                .code(request.getCode())
                .phoneNumber(request.getPhoneNumber())
                .build();
        return memberRepository.save(member);
    }
}

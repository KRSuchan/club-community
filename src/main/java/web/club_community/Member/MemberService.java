package web.club_community.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.club_community.domain.Member;

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
                .username(member.getId())
                .password(member.getPassword())
                .build();
    }
}

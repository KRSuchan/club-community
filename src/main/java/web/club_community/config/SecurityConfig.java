package web.club_community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/member/signup").permitAll()
                                .requestMatchers("/api/callback").permitAll()
                                .requestMatchers("/api/login/kakao-uri").permitAll()
                                .requestMatchers("/api/public/**").hasRole("member")
                                .requestMatchers("/api/club/**").hasRole("member")
                                .requestMatchers("/api/admin/**").hasRole("admin")
                                .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}

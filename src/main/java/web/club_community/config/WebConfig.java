package web.club_community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/*")
                .excludePathPatterns(
                        "/css/**",
                        ".ico",
                        "error",
                        "/api/callback",
                        "/api/login/kakao-uri",
                        "/member/signup",
                        "/member/login"
                );
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/css/**",
                        "/*.ico",
                        "/error",
                        "/api/callback",
                        "/api/login/kakao-uri",
                        "/api/member/signup",
                        "/api/member/login"
                );
    }
}

package perosnal.matcher.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Slf4j
class WebConfig implements WebMvcConfigurer {

    private final String activeProfiles;

    WebConfig(@Value("${spring.profiles.active}") final String activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        if ("local".equals(activeProfiles)) {
            registry.addMapping("/**");
        } else if ("heroku".equals(activeProfiles)) {
            log.info("ACTIVE PROFILE IS HEROKU");
            registry.addMapping("/**")
                    .allowedOrigins("https://dev-rifaii.github.io/");
        } else {
            throw new IllegalStateException("invalid profile " + activeProfiles);
        }
    }

}

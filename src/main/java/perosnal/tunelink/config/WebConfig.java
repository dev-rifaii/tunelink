package perosnal.tunelink.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Slf4j
class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**");
    }
}

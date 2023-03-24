package personal.tunelink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import perosnal.tunelink.Application;
import perosnal.tunelink.spotify.SpotifyApiClient;
import perosnal.tunelink.spotify.SpotifyAuthorizationClient;
import perosnal.tunelink.spotify.SpotifyAuthorizationService;

@ActiveProfiles("itest")
@AutoConfigureMockMvc
@ContextConfiguration(initializers = IntegrationTestBase.ContextInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public abstract class IntegrationTestBase {

    @MockBean
    protected SpotifyAuthorizationClient spotifyAuthorizationClient;
    @MockBean
    protected SpotifyApiClient spotifyApiClient;
    @MockBean
    protected SpotifyAuthorizationService spotifyAuthorizationService;
    @Autowired
    protected MockMvc mockMvc;

    private static final PostgreSQLContainer postgresTestContainer = new PostgreSQLContainer("postgres:15")
            .withUsername("itest")
            .withPassword("itest")
            .withDatabaseName("tunelink");

    static {
        postgresTestContainer.start();
    }

    @Configuration
    public static class ContextInitializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            final String url = postgresTestContainer.getJdbcUrl();
            final String username = postgresTestContainer.getUsername();
            final String password = postgresTestContainer.getPassword();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    configurableApplicationContext,
                    "spring.datasource.url=" + url,
                    "spring.datasource.username=" + username,
                    "spring.datasource.password=" + password
            );
        }
    }
}

package perosnal.tunelink.globals;

import java.util.Set;

public class Constants {

    public static final Set<String> NO_AUTH_ENDPOINTS = Set.of(
            "/api/v1/jwt",
            "/api/v1/spotify-auth/url",
            "/api/v1/spotify-auth/callback",
            "/api/v1/spotify-auth/token",
            "/api/v1/spotify-auth/refresh"
            );
}

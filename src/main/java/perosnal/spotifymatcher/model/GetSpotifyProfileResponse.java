package perosnal.spotifymatcher.model;

import lombok.Getter;

import java.util.List;

@Getter
public final class GetSpotifyProfileResponse {

    private String id;
    private String country;
    private String email;
    private List<SpotifyUserImage> images;

    @Getter
    public static class SpotifyUserImage {
        private String url;
    }

}
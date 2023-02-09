package perosnal.tunelink.spotify.dto;

import java.util.List;

public record SpotifyProfileDto(String id, String country, String email, List<SpotifyUserImage> images) {


    public record SpotifyUserImage(String url) {
    }

}
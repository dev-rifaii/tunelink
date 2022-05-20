package perosnal.spotifymatcher.model;

import java.util.List;

public record GetSpotifyProfileResponse(String id, String country,String email,List<SpotifyUserImage> images ) {


    public record SpotifyUserImage(String url) {
    }

}
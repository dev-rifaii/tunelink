package perosnal.spotifymatcher.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class SpotifyUser {

    private final GetSpotifyProfileResponse profile;
    private final List<String> topTracks;
    private final List<String> topArtists;

}

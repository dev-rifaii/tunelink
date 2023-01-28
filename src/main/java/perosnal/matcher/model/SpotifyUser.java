package perosnal.matcher.model;

import java.util.List;


public record SpotifyUser(SpotifyProfileDto profile, List<String> topTracks, List<String> topArtists) {
}

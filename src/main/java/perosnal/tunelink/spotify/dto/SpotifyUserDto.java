package perosnal.tunelink.spotify.dto;

import java.util.List;


public record SpotifyUserDto(SpotifyProfileDto profile, List<String> topTracks, List<String> topArtists) {
}

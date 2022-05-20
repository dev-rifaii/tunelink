package perosnal.spotifymatcher.model;

import java.util.List;


public record SpotifyUser(GetSpotifyProfileResponse profile,List<String> topTracks,List<String> topArtists ) {
}

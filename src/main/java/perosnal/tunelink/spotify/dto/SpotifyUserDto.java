package perosnal.tunelink.spotify.dto;

public record SpotifyUserDto(
        SpotifyProfileDto profile,
        SpotifyTopTracksDto topTracks,
        SpotifyTopArtistsDto topArtists
) {
}

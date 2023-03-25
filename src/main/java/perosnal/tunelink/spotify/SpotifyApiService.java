package perosnal.tunelink.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perosnal.tunelink.spotify.dto.SpotifyProfileDto;
import perosnal.tunelink.spotify.dto.SpotifyTopArtistsDto;
import perosnal.tunelink.spotify.dto.SpotifyTopTracksDto;
import perosnal.tunelink.spotify.dto.SpotifyUserDto;
import perosnal.tunelink.track.Track;
import perosnal.tunelink.user.User;
import perosnal.tunelink.user.UserService;
import perosnal.tunelink.util.Mappers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static perosnal.tunelink.util.Helpers.appendBearer;


@Service
@RequiredArgsConstructor
public class SpotifyApiService {


    private final UserService userService;
    private final SpotifyApiClient spotifyApiClient;


    @Transactional
    public void persistUser(String token) throws IOException {
        final var spotifyUserDto = fetchUserFromSpotifyApi(token);
        final User user = User.builder()
                .id(spotifyUserDto.profile().id())
                .email(spotifyUserDto.profile().email())
                .tracks(collectTracksIds(spotifyUserDto.topTracks()))
                .artists(collectArtistsIds(spotifyUserDto.topArtists()))
                .country(spotifyUserDto.profile().country())
                .imageUrl(spotifyUserDto.profile()
                        .images()
                        .stream()
                        .findAny()
                        .map(SpotifyProfileDto.SpotifyUserImage::url)
                        .orElse(null))
                .build();

        userService.persistSpotifyUser(user, collectTracksDetails(spotifyUserDto.topTracks()));
    }

    public String getIdByToken(String token) {
        return spotifyApiClient.getProfile(appendBearer(token)).id();
    }

    private SpotifyUserDto fetchUserFromSpotifyApi(String token) {
        final SpotifyProfileDto profile = spotifyApiClient.getProfile(appendBearer(token));
        final SpotifyTopTracksDto topTracks = spotifyApiClient.getTopTracks(appendBearer(token));
        final SpotifyTopArtistsDto topArtists = spotifyApiClient.getTopArtistsId(appendBearer(token));

        return new SpotifyUserDto(profile, topTracks, topArtists);
    }

    private List<String> collectTracksIds(SpotifyTopTracksDto topTracksDto) {
        return topTracksDto.items().stream().map(SpotifyTopTracksDto.Item::id).toList();
    }

    private List<String> collectArtistsIds(SpotifyTopArtistsDto topArtistsDto) {
        return topArtistsDto.items().stream().map(SpotifyTopArtistsDto.Item::id).toList();
    }

    private List<Track> collectTracksDetails(SpotifyTopTracksDto topTracksDto) {
        return topTracksDto.items().stream().map(Mappers::spotifyTopTracksItemToTrack).collect(Collectors.toList());
    }

}

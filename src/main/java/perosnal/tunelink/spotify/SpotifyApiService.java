package perosnal.tunelink.spotify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perosnal.tunelink.spotify.dto.SpotifyProfileDto;
import perosnal.tunelink.spotify.dto.SpotifyUserDto;
import perosnal.tunelink.track.Track;
import perosnal.tunelink.user.User;
import perosnal.tunelink.user.UserService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpotifyApiService {


    private final UserService userService;
    private final SpotifyApiClient spotifyApiClient;


    @Transactional
    public void persistUser(String token) {
        SpotifyUserDto spotifyUserDto = fetchUserFromSpotifyApi(token);
        User user = User.builder()
                .id(spotifyUserDto.profile().id())
                .email(spotifyUserDto.profile().email())
                .tracks(spotifyUserDto.topTracks())
                .artists(spotifyUserDto.topArtists())
                .country(spotifyUserDto.profile().country())
                .imageUrl(spotifyUserDto.profile()
                        .images()
                        .stream()
                        .findAny()
                        .map(SpotifyProfileDto.SpotifyUserImage::url)
                        .orElse(null))
                .build();
        userService.persistSpotifyUser(user, getTracksDetails(user.getTracks(), token));
    }

    public String getIdByToken(String token) {
        return spotifyApiClient.getProfile(token).id();
    }

    private SpotifyUserDto fetchUserFromSpotifyApi(String token) {
        final SpotifyProfileDto profile = spotifyApiClient.getProfile("Bearer " + token);
        final List<String> topTrackIds = spotifyApiClient.getTopTracksId(token);
        final List<String> topArtistsIds = spotifyApiClient.getTopArtistsId(token);

        return new SpotifyUserDto(profile, topTrackIds, topArtistsIds);
    }

    private List<Track> getTracksDetails(List<String> tracksIds, String token) {
        String ids = String.join(",", tracksIds);
        return spotifyApiClient.getTracksDetails(ids, token).tracks()
                .stream()
                .map(track -> Track.builder()
                        .name(track.name())
                        .href(track.external_urls().spotify())
                        .id(track.id())
                        .imageUrl(track.album().images().get(0).url())
                        .build())
                .collect(Collectors.toList());
    }
}

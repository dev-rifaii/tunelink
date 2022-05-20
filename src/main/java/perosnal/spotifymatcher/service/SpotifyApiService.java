package perosnal.spotifymatcher.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perosnal.spotifymatcher.api.SpotifyAPI;
import perosnal.spotifymatcher.model.GetSpotifyProfileResponse;
import perosnal.spotifymatcher.model.SpotifyUser;
import perosnal.spotifymatcher.model.Track;
import perosnal.spotifymatcher.model.User;
import perosnal.spotifymatcher.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpotifyApiService {


    private final UserRepository userRepository;
    private final SpotifyAPI spotifyAPI;

    @Transactional
    public void persistUser(String token) {
        SpotifyUser spotifyUser = fetchUserFromSpotifyApi(token);
        User user = userRepository.findById(spotifyUser.profile()
                        .id())
                .map(existingUser -> {
                    existingUser.setArtists(spotifyUser.topArtists());
                    existingUser.setTracks(spotifyUser.topTracks());

                    spotifyUser.profile()
                            .images()
                            .stream()
                            .findAny()
                            .map(GetSpotifyProfileResponse.SpotifyUserImage::url)
                            .ifPresent(existingUser::setImage);
                    return existingUser;
                })
                .orElseGet(() -> User.builder()
                        .id(spotifyUser.profile()
                                .id())
                        .email(spotifyUser.profile()
                                .email())
                        .tracks(spotifyUser.topTracks())
                        .artists(spotifyUser.topArtists())
                        .country(spotifyUser.profile()
                                .country())
                        .build());

        userRepository.save(user);
    }

    @SneakyThrows
    public String getIdByToken(String token) {
        return spotifyAPI.getProfile(token)
                .id();
    }

    @SneakyThrows
    public SpotifyUser fetchUserFromSpotifyApi(String token) {
        final GetSpotifyProfileResponse profile = spotifyAPI.getProfile(token);
        final List<String> topTrackIds = spotifyAPI.getTopTracksId(token);
        final List<String> topArtistsIds = spotifyAPI.getTopArtistsId(token);

        return new SpotifyUser(profile, topTrackIds, topArtistsIds);
    }

    @SneakyThrows
    public List<Track> getTracksDetails(List<String> tracksIds, String token) {
        return spotifyAPI.getTracksDetails(tracksIds, token).tracks()
                .stream()
                .map(track -> Track.builder()
                        .name(track.name())
                        .href(track.external_urls()
                                .spotify())
                        .id(track.id())
                        .imageUrl(track.album()
                                .images()
                                .get(0)
                                .url())
                        .build())
                .collect(Collectors.toList());
    }


}

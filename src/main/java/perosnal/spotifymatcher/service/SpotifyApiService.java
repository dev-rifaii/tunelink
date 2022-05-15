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
        User user = userRepository.findById(spotifyUser.getProfile()
                        .getId())
                .map(existingUser -> {
                    existingUser.setArtists(spotifyUser.getTopArtists());
                    existingUser.setTracks(spotifyUser.getTopTracks());

                    spotifyUser.getProfile()
                            .getImages()
                            .stream()
                            .findAny()
                            .map(GetSpotifyProfileResponse.SpotifyUserImage::getUrl)
                            .ifPresent(existingUser::setImage);
                    return existingUser;
                })
                .orElseGet(() -> User.builder()
                        .id(spotifyUser.getProfile()
                                .getId())
                        .email(spotifyUser.getProfile()
                                .getEmail())
                        .tracks(spotifyUser.getTopTracks())
                        .artists(spotifyUser.getTopArtists())
                        .country(spotifyUser.getProfile()
                                .getCountry())
                        .build());

        userRepository.save(user);
    }

    @SneakyThrows
    public String getIdByToken(String token) {
        return spotifyAPI.getProfile(token)
                .getId();
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
        return spotifyAPI.getTracksDetails(tracksIds, token).getTracks()
                .stream()
                .map(track -> Track.builder()
                        .name(track.getName())
                        .href(track.getExternal_urls()
                                .getSpotify())
                        .id(track.getId())
                        .imageUrl(track.getAlbum()
                                .getImages()
                                .get(0)
                                .getUrl())
                        .build())
                .collect(Collectors.toList());
    }


}

package perosnal.matcher.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perosnal.matcher.model.SpotifyProfileDto;
import perosnal.matcher.model.SpotifyUser;
import perosnal.matcher.model.Track;
import perosnal.matcher.model.User;
import perosnal.matcher.repository.TrackRepository;
import perosnal.matcher.repository.UserRepository;
import perosnal.matcher.client.SpotifyApiClient;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpotifyApiService {


    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final SpotifyApiClient spotifyApiClient;


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
                            .map(SpotifyProfileDto.SpotifyUserImage::url)
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
                        .image(spotifyUser.profile()
                                .images()
                                .stream()
                                .findAny()
                                .map(SpotifyProfileDto.SpotifyUserImage::url)
                                .orElse(""))
                        .build());

        if (!user.getTracks().isEmpty()) {
            List<Track> tracks = getTracksDetails(user.getTracks(), token);
            trackRepository.saveAll(tracks);
        }


        userRepository.save(user);
    }

    @SneakyThrows
    public String getIdByToken(String token) {
        return spotifyApiClient.getProfile(token)
                .id();
    }

    @SneakyThrows
    public SpotifyUser fetchUserFromSpotifyApi(String token) {
        final SpotifyProfileDto profile = spotifyApiClient.getProfile("Bearer " + token);
        final List<String> topTrackIds = spotifyApiClient.getTopTracksId(token);
        final List<String> topArtistsIds = spotifyApiClient.getTopArtistsId(token);

        return new SpotifyUser(profile, topTrackIds, topArtistsIds);
    }

    @SneakyThrows
    public List<Track> getTracksDetails(List<String> tracksIds, String token) {
        String ids = String.join(",", tracksIds);
        return spotifyApiClient.getTracksDetails(ids, token).tracks()
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

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

import java.util.List;
import java.util.stream.Collectors;

import static perosnal.tunelink.util.Helpers.appendBearer;


@Service
@RequiredArgsConstructor
public class SpotifyProfileService {

    private final UserService userService;
    private final SpotifyProfileClient spotifyProfileClient;

    @Transactional
    public void persistUser(String token) {
        final var spotifyUserDto = fetchUserFromSpotifyApi(token);
        final User user = new User()
                .setId(spotifyUserDto.profile().id())
                .setEmail(spotifyUserDto.profile().email())
                .setTracks(collectTracksIds(spotifyUserDto.topTracks()))
                .setArtists(collectArtistsIds(spotifyUserDto.topArtists()))
                .setCountry(spotifyUserDto.profile().country())
                .setImageUrl(spotifyUserDto.profile()
                        .images()
                        .stream()
                        .findAny()
                        .map(SpotifyProfileDto.SpotifyUserImage::url)
                        .orElse(null));

        userService.persistSpotifyUser(user, collectTracksDetails(spotifyUserDto.topTracks()));
    }

    public String getIdByToken(String token) {
        return spotifyProfileClient.getProfile(appendBearer(token)).id();
    }

    private SpotifyUserDto fetchUserFromSpotifyApi(String token) {
        final SpotifyProfileDto profile = spotifyProfileClient.getProfile(appendBearer(token));
        final SpotifyTopTracksDto topTracks = spotifyProfileClient.getTopTracks(appendBearer(token));
        final SpotifyTopArtistsDto topArtists = spotifyProfileClient.getTopArtistsId(appendBearer(token));

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

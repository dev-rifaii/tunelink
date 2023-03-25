package perosnal.tunelink.util;

import perosnal.tunelink.spotify.dto.SpotifyTopTracksDto;
import perosnal.tunelink.track.Track;

public class Mappers {

    public static Track spotifyTopTracksItemToTrack(SpotifyTopTracksDto.Item item) {
        return new Track()
                .setId(item.id())
                .setName(item.name())
                .setHref(item.href())
                .setImageUrl(item.album()
                        .images()
                        .stream()
                        .findFirst()
                        .map(SpotifyTopTracksDto.Item.Album.Image::url)
                        .orElse(null));
    }
}

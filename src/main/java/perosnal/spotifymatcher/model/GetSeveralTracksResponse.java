package perosnal.spotifymatcher.model;

import lombok.Getter;

import java.util.List;

@Getter
public class GetSeveralTracksResponse {

    private List<Track> tracks;

    @Getter
    public static class Track {
        private SpotifyTrackAlbum album;
        private String id;
        private String name;
        private SpotifyTrackUrl external_urls;

        @Getter
        public static class SpotifyTrackUrl {
            private String spotify;
        }

        @Getter
        public static class SpotifyTrackAlbum {
            private List<SpotifyAlbumImage> images;

        }

        @Getter
        public static class SpotifyAlbumImage {
            private String url;

        }
    }
}

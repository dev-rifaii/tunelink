package perosnal.tunelink.spotify.dto;

import java.util.List;


public record SpotifyTopTracksDto(List<Item> items) {
    public record Item(String id, String name, String href, Album album) {
        public record Album(List<Image> images) {
            public record Image(String url) {
            }
        }
    }
}
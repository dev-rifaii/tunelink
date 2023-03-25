package perosnal.tunelink.spotify.dto;

import java.util.List;

public record SpotifyTopArtistsDto(List<Item> items) {
    public record Item(String id, String name, List<Images> images, Link external_urls) {
        public record Link(String spotify) {
        }

        public record Images(String url) {
        }
    }
}

package perosnal.spotifymatcher.model;

import lombok.Getter;

import java.util.List;

@Getter
public final class GetSpotifyTopItemsResponse {

    private List<SpotifyItem> items;

    @Getter
    public final static class SpotifyItem {
        private String id;


    }

}

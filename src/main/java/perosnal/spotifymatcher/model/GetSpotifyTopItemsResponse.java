package perosnal.spotifymatcher.model;

import java.util.List;

public record GetSpotifyTopItemsResponse (List<SpotifyItem> items){


    public record SpotifyItem (String id) {

    }

}

package perosnal.matcher.model;

import java.util.List;

public record SpotifyTopItemsDto(List<SpotifyItem> items) {


    public record SpotifyItem(String id) {

    }

}

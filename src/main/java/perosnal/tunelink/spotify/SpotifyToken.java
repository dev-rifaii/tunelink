package perosnal.tunelink.spotify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SpotifyToken {

    String access_token;
    String refresh_token;
    Long expires_at;
}

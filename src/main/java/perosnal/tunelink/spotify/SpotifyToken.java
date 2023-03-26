package perosnal.tunelink.spotify;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SpotifyToken {

    private String access_token;
    private String refresh_token;
    private Long expires_at;

}

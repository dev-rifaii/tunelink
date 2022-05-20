package perosnal.spotifymatcher.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GetToken {

    String access_token;
    String refresh_token;
    Long expires_at;
}

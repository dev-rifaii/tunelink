package perosnal.tunelink.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TokenDto {

    String access_token;
    String refresh_token;
    Long expires_at;
}
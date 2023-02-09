package perosnal.tunelink.spotify.dto;

public record TokenRequestDto(String code, String redirect_uri, String grant_type) {

}



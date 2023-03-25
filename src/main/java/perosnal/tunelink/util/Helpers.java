package perosnal.tunelink.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Helpers {

    public static String getCurrentHost() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static String appendBearer(String token) {
        return "Bearer " + token;
    }
}

package perosnal.tunelink.util;

import perosnal.tunelink.exception.TuneLinkException;

public class Assertions {

    public static void isTrue(boolean requirement, String message) {
        if(!requirement) throw new TuneLinkException(message);
    }

}

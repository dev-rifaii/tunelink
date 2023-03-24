package personal.tunelink.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import perosnal.tunelink.exception.InvalidJwtException;
import perosnal.tunelink.exception.TuneLinkException;
import perosnal.tunelink.jwt.JwtManager;
import personal.tunelink.IntegrationTestBase;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtManagerITest extends IntegrationTestBase {

    @Autowired
    JwtManager jwtManager;

    private final String userId = UUID.randomUUID().toString();

    @Test
    void extractSub_IsSuccessful_IfTokenIsValid() {
        final String token = jwtManager.generateToken(userId);
        assertEquals(userId, jwtManager.extractSub(token));
    }

    @Test
    void validateJwt_ThrowsInvalidJwtException_IfTokenIsInvalid() {
        final String token = jwtManager.generateToken(userId) + "X";
        assertThrows(InvalidJwtException.class, () -> jwtManager.validateJwt(token));
    }

    @Test
    void generateToken_ThrowsTuneLinkException_IfUserIdIsNullBlankOrEmpty() {
        assertThrows(TuneLinkException.class, () -> jwtManager.generateToken(null), "User ID can't be empty");
        assertThrows(TuneLinkException.class, () -> jwtManager.generateToken("  "), "User ID can't be empty");
        assertThrows(TuneLinkException.class, () -> jwtManager.generateToken(""), "User ID can't be empty");
    }
}

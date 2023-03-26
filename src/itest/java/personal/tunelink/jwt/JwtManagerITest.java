package personal.tunelink.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import perosnal.tunelink.exceptions.InvalidJwtException;
import perosnal.tunelink.exceptions.TuneLinkException;
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
        final String token = jwtManager.generateJwt(userId);
        assertEquals(userId, jwtManager.extractSub(token));
    }

    @Test
    void validateJwt_ThrowsInvalidJwtException_IfTokenIsInvalid() {
        final String token = jwtManager.generateJwt(userId) + "X";
        assertThrows(InvalidJwtException.class, () -> jwtManager.extractSub(token));
    }

    @Test
    //TODO: Make it parametrized test
    void generateToken_ThrowsTuneLinkException_IfUserIdIsNullBlankOrEmpty() {
        assertThrows(TuneLinkException.class, () -> jwtManager.generateJwt(null), "User ID can't be empty");
        assertThrows(TuneLinkException.class, () -> jwtManager.generateJwt("  "), "User ID can't be empty");
        assertThrows(TuneLinkException.class, () -> jwtManager.generateJwt(""), "User ID can't be empty");
    }
}

package perosnal.spotifymatcher.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import perosnal.spotifymatcher.util.HttpRequestSender;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final HttpRequestSender httpRequestSender;

    @Value("${auth.server.url}")
    private String AUTH_SERVER_URL;


    public void validateJwt(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(publicKey())
                .build()
                .parseClaimsJws(jwt);
    }

    @SneakyThrows
    private PublicKey publicKey() {
        String rsaPublicKey = httpRequestSender.genericRequest(AUTH_SERVER_URL + "/api/key");
        String publicKey = rsaPublicKey.replace("\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        final byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String getUserSpotifyId(String jwt) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(publicKey())
                .build()
                .parseClaimsJws(jwt);
        return claims.getBody().get("user_id").toString();
    }

}

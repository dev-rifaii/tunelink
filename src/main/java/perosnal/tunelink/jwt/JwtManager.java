package perosnal.tunelink.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
public class JwtManager {

    @Value("${rsa.public.key}")
    private String publicKey;

    @Value("${rsa.private.key}")
    private String privateKey;


    public void validateJwt(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(publicKey())
                .build()
                .parseClaimsJws(jwt);
    }

    @SneakyThrows
    public String generateToken(String userId) {
        return Jwts.builder()
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(1)))
                .setClaims(Map.ofEntries(
                        entry("user_id", userId))
                )
                .signWith(getPrivateKey())
                .compact();
    }

    @SneakyThrows
    private PublicKey publicKey() {
        publicKey = publicKey.replace("\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        final byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
        var spec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKey = privateKey.replace("\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }


    public String getUserSpotifyId(String jwt) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(publicKey())
                .build()
                .parseClaimsJws(jwt);
        return claims.getBody().get("user_id").toString();
    }

}

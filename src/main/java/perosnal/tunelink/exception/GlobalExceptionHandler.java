package perosnal.tunelink.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handler(InvalidTokenException e) {
        return new ResponseEntity<>("Unauthorized", UNAUTHORIZED);
    }

    @ExceptionHandler
    ResponseEntity<?> handler(SignatureException e) {
        return new ResponseEntity<>("Invalid Jwt", UNAUTHORIZED);
    }

    @ExceptionHandler
    ResponseEntity<?> handler(TuneLinkException e){
        return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
    }

}

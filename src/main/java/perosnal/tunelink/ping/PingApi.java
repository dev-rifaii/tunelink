package perosnal.tunelink.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import perosnal.tunelink.util.Helpers;

@RestController
@RequestMapping("/ping")
public class PingApi {

    @GetMapping
    public String ping(@RequestHeader String userId) {
        System.out.println(Helpers.getCurrentHost());
        return "pong:" + userId;
    }
}

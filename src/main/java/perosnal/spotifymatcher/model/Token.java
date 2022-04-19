package perosnal.spotifymatcher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "token")
public class Token {
    @Id
    @Column(name = "user_id")
    private String id;

    private String accessToken;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}

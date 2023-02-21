package perosnal.tunelink.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "`user`")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {

    @Id
    private String id;

    private String email;

    private String country;

    private String biography;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "user_top_tracks",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "track_id")
    @JsonIgnore
    private List<String> tracks;

    @ElementCollection
    @CollectionTable(name = "user_top_artists",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "artist_id")
    @JsonIgnore
    private List<String> artists;

    @ElementCollection
    @CollectionTable(name = "matches",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "`match`")
    @JsonIgnore
    private List<String> matches;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "blocked_users",
            joinColumns = @JoinColumn(name = "blocker_id"))
    @Column(name = "blocked_id")
    @JsonIgnore
    private List<String> blocked;

}

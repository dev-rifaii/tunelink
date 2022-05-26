package perosnal.spotifymatcher.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "application_users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User {
    @Id
    private String id;

    private String email;

    private String country;

    private String biography;

    private String image;

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

    @ElementCollection
    @CollectionTable(name = "blocked_users",
            joinColumns = @JoinColumn(name = "blocker_id"))
    @Column(name = "blocked_id")
    @JsonIgnore
    private List<String> blocked;

}

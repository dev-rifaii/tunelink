package perosnal.spotifymatcher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "track_details")
public class Track {
    @Id
    private String id;
    private String name;
    private String href;
    private String imageUrl;
}

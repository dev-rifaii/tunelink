package perosnal.matcher.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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

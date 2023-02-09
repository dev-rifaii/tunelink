package perosnal.tunelink.track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import perosnal.tunelink.track.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
}

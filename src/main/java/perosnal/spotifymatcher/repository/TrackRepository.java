package perosnal.spotifymatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import perosnal.spotifymatcher.model.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
}

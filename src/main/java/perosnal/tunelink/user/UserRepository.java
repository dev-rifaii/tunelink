package perosnal.tunelink.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM user u " +
            "JOIN user_top_tracks utt ON u.id = utt.user_id " +
            "WHERE u.id =?1", nativeQuery = true)
    User getById(String id);


    @Query(value = "SELECT * from user where id IN (" +
            "SELECT that.user_id " +
            "FROM user_top_tracks AS this INNER JOIN user_top_tracks " +
            "AS that ON that.user_id <> this.user_id " +
            "AND that.track_id = this.track_id WHERE this.user_id = ?1 " +
            "GROUP BY that.user_id HAVING COUNT(*) >= ?2 )", nativeQuery = true)
    List<User> getMatches(String userId, int matchingTracks);

}


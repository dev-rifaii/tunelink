package perosnal.spotifymatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import perosnal.spotifymatcher.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    @Query("select t from User u JOIN u.tracks t "
            + "WHERE u.id =?1 ")
    List<String> findTracksById(String id);

    List<User> findByCountry(String country);


    @Query(value ="SELECT * from application_users where id IN ("+
            "SELECT that.user_id " +
            "FROM user_top_tracks AS this INNER JOIN user_top_tracks " +
            "AS that ON that.user_id <> this.user_id " +
            "AND that.track_id = this.track_id WHERE this.user_id = ?1 " +
            "GROUP BY that.user_id HAVING COUNT(*) >= ?2 )", nativeQuery = true)
    List<User> getMatches(String userId, int matchingTracks);


}


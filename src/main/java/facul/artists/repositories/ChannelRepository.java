package facul.artists.repositories;

import facul.artists.models.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    @Query("SELECT c FROM Channel c WHERE " +
            "(c.user1Id = :id1 AND c.user2Id = :id2) OR " +
            "(c.user1Id = :id2 AND c.user2Id = :id1)")
    Optional<Channel> findExistingChannel(@Param("id1") Long id1, @Param("id2") Long id2);

    void deleteByUser1IdOrUser2Id(Long user1Id, Long user2Id);

    List<Channel> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}
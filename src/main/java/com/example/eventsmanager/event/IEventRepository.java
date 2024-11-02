package com.example.eventsmanager.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findAllByOrganiserId(Long organiserId);
    @Query("SELECT e FROM EventEntity e JOIN e.participants p WHERE p.id = :participantId")
    List<EventEntity> findAllByParticipantId(Long participantId);
    @Modifying
    @Query("UPDATE EventEntity a SET a.imageUrl = :imageUrl WHERE a.id = :eventId")
    void updateImageUrl(@Param("eventId") Long eventId, @Param("imageUrl") String imageUrl);

}

package com.project.hutech_event.repository;

import com.project.hutech_event.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration,Long> {
    List<EventRegistration> findByEvent_EventId(Long eventId);
    @Query("SELECT er FROM EventRegistration er WHERE er.event.id = :eventId AND er.user.id = :userId")
    Optional<EventRegistration> findByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId);
}

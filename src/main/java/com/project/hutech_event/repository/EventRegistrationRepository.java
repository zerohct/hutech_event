package com.project.hutech_event.repository;

import com.project.hutech_event.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration,Long> {
    List<EventRegistration> findByEvent_EventId(Long eventId);
}

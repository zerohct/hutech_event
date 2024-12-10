package com.project.hutech_event.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Table(name = "EventTypes")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(nullable = false, length = 100)
    private String typeName;
}

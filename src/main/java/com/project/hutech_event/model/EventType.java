package com.project.hutech_event.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EventTypes")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(nullable = false, length = 100)
    private String typeName;

    public void setTypeId(Long id) {
        this.typeId = id;
    }

    public String getTypeName() {
        return typeName;
    }
}

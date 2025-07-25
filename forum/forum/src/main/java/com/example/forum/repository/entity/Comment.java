package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment2")
@Getter
@Setter
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String comment;

    @Column
    private int contentId;

    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Column(name = "updated_date", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedDate;
}

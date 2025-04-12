package com.elinikon.merrbio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "description")
    private String description;

    @Column(name = "approved")
    private boolean approved;

    @ManyToOne
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.approved = false;
    }
}

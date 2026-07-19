package com.example.orderservice.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String createId;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private String updateId;

    @Column(nullable = false)
    private Timestamp updatedAt;
}
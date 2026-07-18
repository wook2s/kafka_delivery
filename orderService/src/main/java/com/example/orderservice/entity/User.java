package com.example.orderservice.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String email;
    private String phone;

    private String createId;
    private Timestamp createdAt;
    private String updateId;
    private Timestamp updatedAt;
}
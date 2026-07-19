package com.example.orderservice.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;

    private String createId;
    private Timestamp createdAt;
    private String updateId;
    private Timestamp updatedAt;
}
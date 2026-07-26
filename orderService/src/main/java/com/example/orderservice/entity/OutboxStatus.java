package com.example.orderservice.entity;

public enum OutboxStatus {
    READY,
    PUBLISHED,
    TIMEOUT,
    FAILED
}

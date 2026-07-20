#!/bin/bash

# 1. 실제 카프카 자바 프로세스(kafka.Kafka)가 실행 중인지 정확히 확인
KAFKA_PROCESS=$(ps -ef | grep 'kafka\.Kafka' | grep -v grep)

if [ -n "$KAFKA_PROCESS" ]; then
    echo "⚠️ Kafka가 이미 실행 중입니다. 실행을 취소합니다."
    echo "================= [실행 중인 프로세스] ================="
    echo "$KAFKA_PROCESS"
    exit 1
else
    echo "🚀 Kafka를 시작합니다..."
    ./kafka-server-start.sh -daemon ../config/kraft/server.properties
    echo "✅ Kafka가 백그라운드(daemon) 모드로 시작되었습니다."
fi

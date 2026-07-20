#!/bin/bash

# 실행 중인 Kafka 프로세스 PID 찾기
KAFKA_PID=$(ps -ef | grep 'kafka\.Kafka' | grep -v grep | awk '{print $2}')

if [ -z "$KAFKA_PID" ]; then
    echo "ℹ️ 실행 중인 Kafka 프로세스가 없습니다."
    exit 0
fi

echo "🛑 Kafka 프로세스(PID: $KAFKA_PID) 종료를 시작합니다..."

# 2. kafka-server-stop.sh 호출 (정상 종료 요청)
if [ -f "./kafka-server-stop.sh" ]; then
    ./kafka-server-stop.sh
else
    echo "⚠️ kafka-server-stop.sh 스크립트를 찾을 수 없어 직접 SIGTERM(15) 시그널을 보냅니다."
    kill -15 $KAFKA_PID
fi

# 3. 종료 대기 루프 (타임아웃: 30초)
TIMEOUT=30
WAIT_TIME=0

while [ $WAIT_TIME -lt $TIMEOUT ]; do
    # 프로세스가 살아있는지 확인
    STILL_RUNNING=$(ps -p $KAFKA_PID -o pid= | grep -v PID)
    
    if [ -z "$STILL_RUNNING" ]; then
        echo "✅ Kafka가 성공적으로 정상 종료되었습니다."
        exit 0
    fi

    echo "⏳ Kafka 데이터 정리 및 종료 대기 중... (${WAIT_TIME}/${TIMEOUT}초)"
    sleep 3
    WAIT_TIME=$((WAIT_TIME + 3))
done

# 4. 타임아웃 초과 시 강제 종료 처리
echo "⚠️ 지정된 시간(${TIMEOUT}초) 내에 정상 종료되지 않았습니다."
echo "🚨 안전을 위해 강제 종료(kill -9)를 진행합니다..."
kill -9 $KAFKA_PID
sleep 1

# 최종 확인
STILL_RUNNING=$(ps -p $KAFKA_PID -o pid= | grep -v PID)
if [ -z "$STILL_RUNNING" ]; then
    echo "✅ Kafka가 강제 종료되었습니다."
else
    echo "❌ Kafka 종료에 실패했습니다. PID: $KAFKA_PID 의 상태를 직접 확인해주세요."
    exit 1
fi

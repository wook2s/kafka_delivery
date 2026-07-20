package com.example.orderservice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleConsumer {

    //@KafkaListener(topics = "order-requested", groupId = "sample")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String payload) {
        log.info("consume key : {}, Payload : {}", eventId, payload);
    }
}

package com.ankoma88.datastructures.performance.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaMessageListener {
    private static final Logger LOGGER = Logger.getLogger(KafkaMessageListener.class.getName());

    @KafkaListener(topics = "topic1", groupId = "group1", autoStartup = "false")
    public void listen(String message) {
        LOGGER.info(message);
    }
}

package com.ilyap.authservice.service.producer;

import com.ilyap.authservice.configuration.KafkaTestConfiguration;
import com.ilyap.authservice.dto.VerificationEmailMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {KafkaTestConfiguration.class, EmailSendingProducer.class})
@RequiredArgsConstructor
public class EmailSendingProducerIT {

    private final KafkaContainer kafkaContainer;
    private final EmailSendingProducer emailSendingProducer;
    private KafkaConsumer<String, VerificationEmailMessage> consumer;

    private static final VerificationEmailMessage VALID_MESSAGE = new VerificationEmailMessage("test@example.com", "https://localhost/verify?code=VERY-SECRET-CODE");

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("email-sending-queue"));
    }

    @Test
    public void sendEmail_validMessage_sendsSuccess() {
        emailSendingProducer.sendEmail(VALID_MESSAGE);

        var record = KafkaTestUtils.getSingleRecord(consumer, "email-sending-queue", Duration.ofSeconds(10));
        assertThat(record).isNotNull();
        assertThat(record.value()).isEqualTo(VALID_MESSAGE);
    }
}

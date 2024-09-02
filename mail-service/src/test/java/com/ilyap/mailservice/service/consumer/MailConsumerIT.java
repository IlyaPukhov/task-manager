package com.ilyap.mailservice.service.consumer;

import com.ilyap.mailservice.KafkaBaseTest;
import com.ilyap.mailservice.dto.VerificationEmailMessage;
import com.ilyap.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
public class MailConsumerIT extends KafkaBaseTest {

    private final ConsumerFactory<String, VerificationEmailMessage> consumerFactory;
    private KafkaTemplate<String, VerificationEmailMessage> kafkaTemplate;
    private Consumer<String, VerificationEmailMessage> consumer;

    @MockBean
    private MailService mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    private static final VerificationEmailMessage VALID_MESSAGE = new VerificationEmailMessage("test@example.com", "https://localhost/verify?code=VERY-SECRET-CODE");
    private static final String TOPIC = "email-sending-queue";

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 10);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(TOPIC));
    }

    @Test
    public void consume_validMessage_processesMessageSuccessfully() {
        Executors.newSingleThreadExecutor().execute(() -> kafkaTemplate.send(TOPIC, VALID_MESSAGE));

        var record = KafkaTestUtils.getSingleRecord(consumer, TOPIC, Duration.ofSeconds(100));
        assertThat(record).isNotNull();
        assertThat(record.value()).isEqualTo(VALID_MESSAGE);
    }
}

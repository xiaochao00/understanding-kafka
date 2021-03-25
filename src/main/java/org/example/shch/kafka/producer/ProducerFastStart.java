package org.example.shch.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * kafka producer
 *
 * @author shichao
 * @since 1.0.0
 * 2021/3/26 0:00
 */
public class ProducerFastStart {
    private static final String BROKERS = "localhost:9092";
    private static final String TOPIC = "topic-demo";

    private static Properties intiConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        Properties prop = intiConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello kafka!");
        producer.send(record);
        producer.close();
    }
}

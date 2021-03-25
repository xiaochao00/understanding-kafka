package org.example.shch.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * kafka consumer
 *
 * @author shichao
 * @since 1.0.0
 * 2021/3/26 0:16
 */
public class ConsumerFastStart {
    private static final String BROKERS = "localhost:9092";
    private static final String TOPIC = "topic-demo";
    private static final String TOPIC_GROUP = "group.demo";

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, TOPIC_GROUP);
        return properties;
    }

    public static void main(String[] args) {
        Properties prop = initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Collections.singleton(TOPIC));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }
}

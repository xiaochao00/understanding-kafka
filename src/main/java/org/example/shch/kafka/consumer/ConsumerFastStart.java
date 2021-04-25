package org.example.shch.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

/**
 * kafka consumer
 *
 * @author shichao
 * @since 1.0.0
 * 2021/3/26 0:16
 */
public class ConsumerFastStart {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerFastStart.class);
    private static final String BROKERS = "localhost:9092";
    private static final String TOPIC = "topic-demo";
    private static final String TOPIC_GROUP = "group.demo";

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, TOPIC_GROUP);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 2000);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2);
//        properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,)
//        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 1000);
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RandomAssignor.class.getName());
        return properties;
    }

    public static void main(String[] args) throws InterruptedException {
        TopicPartition partition = new TopicPartition(TOPIC, 0);
        Properties prop = initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Collections.singleton(TOPIC));
        logger.info("Begin consumer..");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            logger.info("Poll record count:{}.", records.count());
            for (ConsumerRecord<String, String> record : records) {
                logger.info("poll record offset:{},value:{}", record.offset(), record.value());
                Thread.sleep(10000);
                // throw new IllegalArgumentException("here exception");
            }
            long offset = consumer.position(partition);
            logger.info("Current offset:{}", offset);
        }
    }
}

package org.example.shch.kafka.consumer;

import org.apache.kafka.clients.consumer.internals.AbstractPartitionAssignor;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 随机 分区分配器
 *
 * @author shichao
 * @since 1.0.0
 * 2021/4/21 23:56
 */
public class RandomAssignor extends AbstractPartitionAssignor {


    @Override
    public Map<String, List<TopicPartition>> assign(Map<String, Integer> partitionsPerTopic, Map<String, Subscription> subscriptions) {
        Map<String, List<String>> consumersPerTopic = this.getConsumersPerTopic(subscriptions);

        Map<String, List<TopicPartition>> assignments = new HashMap<>();
        for (String consumerId : subscriptions.keySet()) {
            assignments.put(consumerId, new ArrayList<>());
        }
        // 为每个topic的分区，分配消费者
        consumersPerTopic.forEach((topic, consumerIds) -> {
            Integer partitionNum = partitionsPerTopic.get(topic);
            if (partitionNum == null) {
                return;
            }
            // 计算topic的所有分区信息
            List<TopicPartition> partitions = AbstractPartitionAssignor.partitions(topic, partitionNum);
            // 为每个分区随机分配一个消费者
            for (TopicPartition partition : partitions) {
                int rand = new Random().nextInt(consumerIds.size());
                String randomConsumerId = consumerIds.get(rand);
                assignments.get(randomConsumerId).add(partition);
            }
        });
        return assignments;
    }

    /**
     * 获取每个主题与其消费者的关系
     *
     * @param subscriptions 每个消费者的订阅信息
     * @return [topic:[consumer1,],]
     */
    private Map<String, List<String>> getConsumersPerTopic(Map<String, Subscription> subscriptions) {
        Map<String, List<String>> topicConsumersMap = new HashMap<>();
        subscriptions.forEach((consumerId, subscription) -> {
            for (String topic : subscription.topics()) {
                if (!topicConsumersMap.containsKey(topic)) {
                    topicConsumersMap.put(topic, new ArrayList<>());
                }
                topicConsumersMap.get(topic).add(consumerId);
            }
        });
        return topicConsumersMap;
    }

    @Override
    public String name() {
        return "random";
    }
}

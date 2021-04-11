package org.example.shch.kafka.admin;

import kafka.controller.NewPartition;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * kafka api
 *
 * @author shichao
 * @since 1.0.0
 * 2021/4/10 22:31
 */
public class KafkaAdminDemo {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdminDemo.class);

    private static final String BROKER_LIST = "127.0.0.1:9092";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        createTopic();
        alterConfig();
        describeConfigs();
    }

    private static void addPartitions() throws ExecutionException, InterruptedException {
        String topic = "topic-admin-api";
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // 该方法的本质是调用 KafkaAdminClient.createInternal方法，并且返回 KafkaAdminClient 对象
        AdminClient client = AdminClient.create(properties);
        //
        NewPartitions partitions = NewPartitions.increaseTo(4);
        Map<String, NewPartitions> newPartitionsMap = new HashMap<>();
        newPartitionsMap.put(topic, partitions);
        CreatePartitionsResult result = client.createPartitions(newPartitionsMap);
        result.all().get();
    }

    private static void alterConfig() throws ExecutionException, InterruptedException {
        String topic = "topic-admin-api";
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // 该方法的本质是调用 KafkaAdminClient.createInternal方法，并且返回 KafkaAdminClient 对象
        AdminClient client = AdminClient.create(properties);
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topic);
        //
        ConfigEntry configEntry = new ConfigEntry("cleanup.policy", "compact");
        AlterConfigOp alterConfigOp = new AlterConfigOp(configEntry, AlterConfigOp.OpType.SET);
        Map<ConfigResource, Collection<AlterConfigOp>> configMap = new HashMap<>();
        configMap.put(resource, Collections.singletonList(alterConfigOp));
        AlterConfigsResult result = client.incrementalAlterConfigs(configMap);
        result.all().get();
        logger.info("Alter config success.");
    }

    private static void describeConfigs() {
        String topic = "topic-admin-api";
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // 该方法的本质是调用 KafkaAdminClient.createInternal方法，并且返回 KafkaAdminClient 对象
        AdminClient client = AdminClient.create(properties);
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topic);
        DescribeConfigsResult result = client.describeConfigs(Collections.singleton(resource));
        try {
            Config config = result.all().get().get(resource);
            logger.info("config:{}.", config);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void createTopic() {
        String topic = "topic-admin-api";
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        // 该方法的本质是调用 KafkaAdminClient.createInternal方法，并且返回 KafkaAdminClient 对象
        AdminClient client = AdminClient.create(properties);
        // 创建4个分区，1个副本；还可以设置副本的分配策略，还可以设置主题的相关配置
        NewTopic newTopic = new NewTopic(topic, 4, (short) 1);
        // CreateTopicsResult 中的方法主要是对成员变量  Map<String, KafkaFuture<TopicMetadataAndConfig>> futures 的操作；
        // 它的key表示主题，value表示创建对应主题的响应；可以一次性创建多个主题；
        // XXXTopics的返回值是XXXTopicsResult，listTopics返回值为ListTopicsResponse
        CreateTopicsResult response = client.createTopics(Collections.singleton(newTopic));
        try {
            response.all().get();
            logger.info("Create topic:{} success.", topic);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

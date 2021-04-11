package org.example.shch.kafka.producer;

/**
 * TopicCommand
 *
 * @author shichao
 * @since 1.0.0
 * 2021/4/9 0:59
 */
public class TopicCommandDemo {
    public static void main(String[] args) {
        String[] options = new String[]{
                "--zookeeper", "localhost:2128/kafka",
                "--create",
                "--replication-factor", "1",
                "--partitions", "3",
                "--topic", "create-topic-api"
        };
        kafka.admin.TopicCommand.main(options);

    }
}

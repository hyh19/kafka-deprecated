import io.vertx.core.Vertx;
import io.vertx.kafka.client.common.PartitionInfo;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 获取主题的分区信息
 */
public class GetTopicPartitionInfo {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "159.65.128.10:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my_group"); // 加入一个消费组
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(Vertx.vertx(), config);

        consumer.partitionsFor("number", ar -> {
            if (ar.succeeded()) {
                System.out.println("获取指定主题的分区信息");
                for (PartitionInfo partitionInfo : ar.result()) {
                    System.out.println(partitionInfo);
                }
            }
        });

        consumer.listTopics(ar -> {
            if (ar.succeeded()) {
                System.out.println("获取所有主题的分区信息");
                Map<String, List<PartitionInfo>> map = ar.result();
                map.forEach((topic, partitions) -> {
                    System.out.println("topic = " + topic);
                    System.out.println("partitions = " + map.get(topic));
                });
            }
        });
    }
}
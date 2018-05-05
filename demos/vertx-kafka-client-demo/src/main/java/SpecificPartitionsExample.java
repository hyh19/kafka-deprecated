import io.vertx.core.Vertx;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 向服务器申请分配特定的主题分区
 */
public class SpecificPartitionsExample {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "159.65.128.10:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my_group"); // 加入一个消费组
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(Vertx.vertx(), config);

        consumer.handler(record -> {
            System.out.println("Processing key=" + record.key() +
                    ",value=" + record.value() +
                    ",partition=" + record.partition() +
                    ",offset=" + record.offset());
        });

        // 自己想要被分配到的主题分区
        Set<TopicPartition> topicPartitions = new HashSet<>();
        topicPartitions.add(new TopicPartition()
                .setTopic("number")
                .setPartition(0));

        // 向服务器申请表把自己分配到特定的主题分区
        consumer.assign(topicPartitions, done -> {
            if (done.succeeded()) {
                System.out.println("Partition assigned");
                // 查看被分配到的主题分区
                consumer.assignment(done1 -> {
                    if (done1.succeeded()) {
                        for (TopicPartition topicPartition : done1.result()) {
                            System.out.println(topicPartition.getTopic() + " " + topicPartition.getPartition());
                        }
                    }
                });
            }
        });


    }
}

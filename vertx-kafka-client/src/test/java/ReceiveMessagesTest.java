import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;

import java.util.*;

/**
 * 接收消息
 */
public class ReceiveMessagesTest {
    @Test
    public void test() throws Exception {

        // 使用 Properties 进行配置，推荐这种方式。
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "128.199.91.70:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my_group"); // 加入一个消费组
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // 使用 Map 进行配置
        /*Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "128.199.91.70:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "my_group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");*/

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(Vertx.vertx(), config);

        consumer.handler(record -> {
            System.out.println("Processing key=" + record.key() +
                    ",value=" + record.value() +
                    ",partition=" + record.partition() +
                    ",offset=" + record.offset());
        });

        // 订阅多个主题
        Set<String> topics = new HashSet<>();
        topics.add("fruit");
        topics.add("animal");
        topics.add("number");

        // 订阅单个主题
        // consumer.subscribe("number");

        consumer.subscribe(topics, ar -> {
            if (ar.succeeded()) {
                System.out.println("subscribed");
            } else {
                System.out.println("Could not subscribe " + ar.cause().getMessage());
            }
        });
        // 不带回调函数
        // consumer.subscribe(topics);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
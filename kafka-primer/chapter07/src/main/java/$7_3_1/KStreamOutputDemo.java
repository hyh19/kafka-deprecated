package $7_3_1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;

/**
 * 7.3.1 创建 KStream 日志流并输出数据集到控制台
 */
public class KStreamOutputDemo {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        // Streams 的 ID 将作为其内置消费组的 ID
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "KStreamOutputDemo");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "206.189.144.39:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // 配置内部消费者的属性
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> stream = builder.stream("banana");
        stream.print();

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        Thread.sleep(Integer.MAX_VALUE);
        // streams.close();
    }
}

package $7_3_1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Properties;

/**
 * 7.3.1 创建 KTable 更新流并输出数据集到控制台
 */
public class KTableOutputDemo {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "KTableOutputDemo");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "206.189.144.39:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        KTable<String, String> table = builder.table("dog", "my_store");
        table.print();

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        Thread.sleep(Integer.MAX_VALUE);
        // streams.close();
    }
}

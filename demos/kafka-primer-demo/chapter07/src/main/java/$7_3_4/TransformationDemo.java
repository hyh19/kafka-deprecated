package $7_3_4;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.Predicate;

import java.util.Date;
import java.util.Properties;

/**
 * Created by huangyh2 on 2018/5/2.
 */
public class TransformationDemo {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "TransformationDemo"); // "StreamLeftJoinDemo", "StreamOuterJoinDemo"
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "188.166.244.4:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> stream = builder.stream("word");
        KStream<String, String> filteredStream = stream.filter(new Predicate<String, String>() {
            @Override
            public boolean test(String key, String value) {
                if (StringUtils.isBlank(value)) {
                    return false;
                }
                return true;
            }
        });
        filteredStream.print();

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        // 每 5 秒打印一次时间
        int counter = 1;
        while (true) {
            System.out.println(counter + " " + new Date().toString());
            Thread.sleep(5000L);
            counter++;
            if (counter > 6) {
                System.out.println("---- 我是分隔线 ----");
                counter = 1;
            }
        }
    }
}

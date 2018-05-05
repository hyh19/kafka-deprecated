package $7_3_3;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 7.3.3 KStream 的连接操作
 */
public class StreamJoinDemo {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "StreamJoinDemo"); // "StreamLeftJoinDemo", "StreamOuterJoinDemo"
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        // 将要进行连接操作的两个数据流
        KStream<String, String> leftStream = builder.stream("left-stream");
        KStream<String, String> rightStream = builder.stream("right-stream");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        KStream<String, String> joinedStream = leftStream.join(rightStream, // leftJoin(), outerJoin
                (leftValue, rightValue) -> "[" + leftValue + ", " + rightValue + "]", JoinWindows.of(TimeUnit.MINUTES.toMillis(5)));
        joinedStream.print();

        Thread.sleep(Integer.MAX_VALUE);
    }
}

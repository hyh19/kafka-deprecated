package $7_3_3;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Date;
import java.util.Properties;

/**
 * 7.3.3 KTable 的连接
 */
public class TableJoinDemo {

    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "TableJoinDemo");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG), "earliest");

        KStreamBuilder builder = new KStreamBuilder();
        KTable<String, String> leftTable = builder.table("left-table", "my_store_left");
        KTable<String, String> rightTable = builder.table("right-table", "my_store_right");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        KTable<String, String> joinedTable = leftTable.join(rightTable, // leftJoin(), outerJoin
                (leftValue, rightValue) -> "[" + leftValue + ", " + rightValue + "]");
        joinedTable.print();

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

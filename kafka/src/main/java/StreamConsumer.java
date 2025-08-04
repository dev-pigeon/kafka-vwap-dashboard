import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.kstream.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamConsumer {
    private final static Logger log = LoggerFactory.getLogger(StreamConsumer.class);
    private static final String STORE_NAME = "vwap-store";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-dashboard-test-" + System.currentTimeMillis());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StockRecordSerde.class);
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2); // each stock is processed
                                                                                             // once

        consume(props);
    }

    private static void consume(Properties props) {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.addStateStore(Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(STORE_NAME),
                Serdes.String(),
                new WindowSerde()));
        KStream<String, StockRecord> stream = streamsBuilder.stream("stock-records");

        stream.transform(() -> new WindowTransformer(), Named.as("WindowTransformer"), STORE_NAME);

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down stream...");
            streams.close();
        }));

        try {
            streams.start();
        } catch (Throwable e) {
            log.error("Error while starting StreamConsumer: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}

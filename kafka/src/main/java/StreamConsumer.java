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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StreamConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-dashboard-test-" + System.currentTimeMillis());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StockRecordSerde.class);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, StockRecord> stream = streamsBuilder.stream("stock-records");

        stream
                .groupByKey(Grouped.with(Serdes.String(), new StockRecordSerde()))
                .aggregate(
                        Window::new,
                        (key, value, aggregate) -> {
                            aggregate.updateWindow(value, System.currentTimeMillis());
                            return aggregate;
                        },
                        Materialized.<String, Window, KeyValueStore<Bytes, byte[]>>as("vwap-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new WindowSerde()))
                .suppress(Suppressed.untilTimeLimit(Duration.ofSeconds(10), Suppressed.BufferConfig.unbounded()))
                .toStream()
                .mapValues(window -> {
                    double vwap = window.calculateVWAP();
                    return vwap;
                }).foreach((ticker, vwap) -> {
                    if (vwap != null && !Double.isNaN(vwap)) {
                        try (Connection conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/kafka_dashboard",
                                "kafka_user",
                                "kafka_password");
                                PreparedStatement stmt = conn.prepareStatement(
                                        "INSERT INTO kafka_dashboard.stock_vwap (ticker, vwap) VALUES (?, ?);")) {

                            stmt.setString(1, ticker);
                            stmt.setDouble(2, vwap);
                            stmt.executeUpdate();

                        } catch (SQLException e) {
                            System.err.println("PostgreSQL insert error: " + e.getMessage());
                        }
                    }
                });

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down stream...");
            streams.close();
        }));

        try {
            streams.start();
        } catch (Throwable e) {
            System.err.println("Error while starting Kafka Streams:");
            e.printStackTrace();
        }
    }
}

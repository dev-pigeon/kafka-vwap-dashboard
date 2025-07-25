
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

public class TestConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", "StockRecordDeserializer");
        props.put("group.id", "kafka-dashboard");

        KafkaConsumer<String, StockRecord> consumer = new KafkaConsumer<>(props);
        try {
            consumer.subscribe(Collections.singletonList("stock-records"));

            System.out.println("TestConsumer started...");
            while (true) {
                ConsumerRecords<String, StockRecord> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, StockRecord> record : records) {
                    System.out.printf("ECHOING: %s", record.toString());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error echoing messages", e);
        } finally {
            consumer.close();
        }
    }

}

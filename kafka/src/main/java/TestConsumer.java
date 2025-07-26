
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.DoubleDeserializer;

public class TestConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", DoubleDeserializer.class.getName());
        props.put("group.id", "kafka-dashboard");

        KafkaConsumer<String, Double> consumer = new KafkaConsumer<>(props);
        try {
            consumer.subscribe(Collections.singletonList("record-VWAPs"));

            System.out.println("TestConsumer started...");
            while (true) {
                ConsumerRecords<String, Double> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Double> record : records) {
                    System.out.printf("KEY: %s VALUE: %f", record.key(), record.value());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error echoing messages", e);
        } finally {
            consumer.close();
        }
    }

}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import StockRecord.StockRecord;

import org.apache.kafka.clients.producer.*;

public class StreamProducer {
    private static Long rate = null;
    private static final long DEFAULT_RATE = 250;
    private static final String TOPIC = "stock-records";
    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);

    public static void main(String[] args) {

        if (args.length > 0) {
            String rate_string = args[0];
            rate = Long.parseLong(rate_string);
        }
        streamFile();
    }

    private static void streamFile() {
        rate = (long) ((rate != null) ? rate : DEFAULT_RATE);
        String filePath = "./data/stock_data_cleaned.csv";
        log.info("Opening file...");

        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka-broker:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "StockRecord.StockRecordSerializer");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        try (Producer<String, StockRecord> producer = new KafkaProducer<>(props);
                BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                StockRecord stockRecord = parseLine(line);
                String key = stockRecord.getTicker();
                ProducerRecord<String, StockRecord> record = new ProducerRecord<>(TOPIC, key, stockRecord);
                producer.send(record);
                log.info("Sent Record: {}", stockRecord.toString());
                Thread.sleep(rate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static StockRecord parseLine(String line) {
        String values[] = line.split(",");
        String ticker = values[1];
        double price = Double.parseDouble(values[5]);
        double volume = Double.parseDouble(values[6]);
        long timeStamp = System.currentTimeMillis();
        StockRecord stockRecord = new StockRecord(ticker, price, volume, timeStamp);
        return stockRecord;
    }
}

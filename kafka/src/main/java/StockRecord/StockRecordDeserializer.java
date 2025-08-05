package StockRecord;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class StockRecordDeserializer implements Deserializer<StockRecord> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public StockRecord deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return objectMapper.readValue(data, StockRecord.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing MyCustomObject", e);
        }
    }
}

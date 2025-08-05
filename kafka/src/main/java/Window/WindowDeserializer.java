package Window;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class WindowDeserializer implements Deserializer<Window> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Window deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return objectMapper.readValue(data, Window.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing MyCustomObject", e);
        }
    }
}

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class WindowSerde implements Serde<Window> {
    private final WindowSerializer serdeSerializer = new WindowSerializer();
    private final WindowDeserializer serdeDeserializer = new WindowDeserializer();

    @Override
    public Serializer<Window> serializer() {
        return serdeSerializer;
    }

    @Override
    public Deserializer<Window> deserializer() {
        return serdeDeserializer;
    }

}

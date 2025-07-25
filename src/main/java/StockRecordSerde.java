import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class StockRecordSerde implements Serde<StockRecord> {
    private final StockRecordSerializer serdeSerializer = new StockRecordSerializer();
    private final StockRecordDeserializer serdeDeserializer = new StockRecordDeserializer();

    @Override
    public Serializer<StockRecord> serializer() {
        return serdeSerializer;
    }

    @Override
    public Deserializer<StockRecord> deserializer() {
        return serdeDeserializer;
    }

}

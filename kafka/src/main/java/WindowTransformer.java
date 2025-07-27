import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.Cancellable;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WindowTransformer implements Transformer<String, StockRecord, KeyValue<String, Double>> {
    private final Logger log = LoggerFactory.getLogger(WindowTransformer.class);
    private ProcessorContext context;
    private KeyValueStore<String, Window> store;
    private Cancellable punctuator;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.store = (KeyValueStore<String, Window>) context.getStateStore("vwap-store");
        this.punctuator = context.schedule(Duration.ofSeconds(20), PunctuationType.WALL_CLOCK_TIME, this::punctuate);
    }

    @Override
    public KeyValue<String, Double> transform(String key, StockRecord value) {
        Window window = store.get(key);
        if (window == null) {
            window = new Window();
        }
        window.updateWindow(value, System.currentTimeMillis());
        store.put(key, window);
        return null; 
    }

    private void punctuate(long timestamp) {
        try (KeyValueIterator<String, Window> iter = store.all()) {
            while (iter.hasNext()) {
                KeyValue<String, Window> entry = iter.next();
                Window window = entry.value;
              //  window.checkRecordMembership(timestamp);
                double vwap = window.calculateVWAP();
                context.forward(entry.key, vwap);
                store.put(entry.key, window);
            }
        }
        context.commit();
    }

    @Override
    public void close() {
        if (punctuator != null) {
            punctuator.cancel();
        }
    }
}

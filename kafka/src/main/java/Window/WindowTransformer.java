import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.Cancellable;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class WindowTransformer implements Transformer<String, StockRecord, KeyValue<String, Double>> {
    private final Logger log = LoggerFactory.getLogger(WindowTransformer.class);
    private final String STORE_NAME = "vwap-store";
    private final int INTERVAL = 20; // seconds
    private final String DB_URL = "jdbc:postgresql://localhost:5432/kafka_dashboard";
    private final String DB_USER = "kafka_user";
    private final String DB_PWD = "kafka_password";
    private final int BATCH_SIZE = 250;
    private ProcessorContext context;
    private KeyValueStore<String, Window> store;
    private Cancellable punctuator;
    private ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.store = (KeyValueStore<String, Window>) context.getStateStore(STORE_NAME);
        this.punctuator = context.schedule(Duration.ofSeconds(INTERVAL), PunctuationType.WALL_CLOCK_TIME,
                this::punctuate);
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
                Double vwap = window.calculateVWAP();
                if (vwap != null && !Double.isNaN(vwap)) {
                    cache.put(entry.key, vwap);
                }
                store.put(entry.key, window); // leave this in
            }
        }
        context.commit();
        batchInsert(timestamp);
        cache.clear();
    }

    private void batchInsert(long timestamp) {
        log.info("Initializing batch insert...");
        final String query = "INSERT INTO kafka_dashboard.stock_vwap (ticker, vwap, last_updated) " +
                "VALUES (?, ?, NOW()) " +
                "ON CONFLICT (ticker) DO UPDATE SET vwap = EXCLUDED.vwap, last_updated = NOW();";
        int counter = 0;
        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
                PreparedStatement statement = conn.prepareStatement(query);) {
            for (Map.Entry<String, Double> entry : cache.entrySet()) {
                statement.setString(1, entry.getKey());
                statement.setDouble(2, entry.getValue());
                statement.addBatch();
                ++counter;
                if (counter >= BATCH_SIZE) {
                    // artificially control batch size
                    statement.executeBatch();
                    counter = 0;
                }
            }
            statement.executeBatch();
            log.info("batch insert complete");
        } catch (SQLException e) {
            log.error("PostgreSQL insert error: {}", e);
        }
    }

    @Override
    public void close() {
        if (punctuator != null) {
            punctuator.cancel();
        }
    }
}

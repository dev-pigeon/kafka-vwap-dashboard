import java.util.ArrayDeque;
import java.util.Deque;

public class Window {
    private double runningProductSum = 0.0; // The running sum of volume x price for each record
    private double runningVolumeSum = 0.0;
    private final double RECORD_LIFESPAN = 2000;
    private Deque<StockRecord> recordDeque = new ArrayDeque<>();

    public void addRecord(StockRecord record) {

    }

    public boolean recordIsValid(long recordTimeStamp) {
        return false;
    }

    public void evictOldRecords() {

    }

    public double calculateVWAP() {
        return 0.0;
    }
}

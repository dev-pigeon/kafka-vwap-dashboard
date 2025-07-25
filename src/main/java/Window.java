import java.util.ArrayDeque;
import java.util.Deque;

public class Window {
    private double runningProductSum = 0.0; // The running sum of volume x price for each record
    private double runningVolumeSum = 0.0;
    private final double RECORD_LIFESPAN = 2000;
    private Deque<StockRecord> recordDeque = new ArrayDeque<>();

    public void addRecord(StockRecord record) {

    }

    public boolean recordIsValid(long recordTimeStamp, long now) {
        if ((now - recordTimeStamp) < RECORD_LIFESPAN) {
            return true;
        }
        return false;
    }

    public void evictOldRecords() {
        final long now = System.currentTimeMillis();
        while (true && (recordDeque.peek() != null)) {
            StockRecord front = recordDeque.peek();
            if (!(recordIsValid(front.getTimeStamp(), now))) {
                runningProductSum -= (front.getClosePrice() * front.getVolume());
                runningVolumeSum -= front.getVolume();
                recordDeque.pop();
            } else {
                // they are inserted in order - if the front isn't expired neither are the rest
                break;
            }

        }
    }

    public double calculateVWAP() {
        return runningProductSum / runningVolumeSum;
    }
}

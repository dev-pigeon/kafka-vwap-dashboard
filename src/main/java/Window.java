import java.util.ArrayDeque;
import java.util.Deque;

public class Window {
    private double runningProductSum = 0.0; // The running sum of volume x price for each record
    private double runningVolumeSum = 0.0;
    private final double RECORD_LIFESPAN = 2000;
    private Deque<StockRecord> recordDeque = new ArrayDeque<>();

    public void updateWindow(StockRecord record, long now) {
        double recordVolume = record.getVolume();
        runningProductSum += (record.getClosePrice() * recordVolume);
        runningVolumeSum += recordVolume;
        recordDeque.add(record);
        checkRecordMembership(now);
    }

    public void addRecord(StockRecord record) {
        recordDeque.add(record);
    }

    public boolean recordIsValid(long recordTimeStamp, long now) {
        if ((now - recordTimeStamp) < RECORD_LIFESPAN) {
            return true;
        }
        return false;
    }

    public void evictRecord(StockRecord record) {
        double recordVolume = record.getVolume();
        runningProductSum -= (record.getClosePrice() * recordVolume);
        runningVolumeSum -= recordVolume;
        recordDeque.pop();
    }

    public void checkRecordMembership(long now) {
        while (true && (recordDeque.peek() != null)) {
            StockRecord front = recordDeque.peek();
            if (!(recordIsValid(front.getTimeStamp(), now))) {
                evictRecord(front);
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

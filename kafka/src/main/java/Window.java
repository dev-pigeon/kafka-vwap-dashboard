import java.util.Deque;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Window {

    private double runningProductSum = 0.0; // The running sum of volume x price for each record
    private double runningVolumeSum = 0.0;
    private final double RECORD_LIFESPAN = 120_000;
    private final double MIN_VOLUME_THRESHOLD = 1e-6;
    private final Logger log = LoggerFactory.getLogger(Window.class);

    private Deque<StockRecord> recordDeque = new LinkedList<>();

    // No-args constructor for Jackson
    public Window() {
    }

    public Deque<StockRecord> getRecords() {
        return recordDeque;
    }

    public double size() {
        return recordDeque.size();
    }

    public double getRunningProductSum() {
        return runningProductSum;
    }

    public double getRunningVolumeSum() {
        return runningVolumeSum;
    }

    public void updateWindow(StockRecord record, long now) {
        double recordVolume = record.getVolume();
        runningProductSum += (record.getClosePrice() * recordVolume);
        runningVolumeSum += recordVolume;
        recordDeque.add(record);
        // checkRecordMembership(now);
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

        while (!recordDeque.isEmpty()) {
            StockRecord front = recordDeque.peek();
            long age = now - front.getTimeStamp();
            if (!recordIsValid(front.getTimeStamp(), now)) {
                // log.info("Evicting record: {}", age);
                evictRecord(front);
            } else {
                break;
            }
        }

    }

    public double calculateVWAP() {
        checkRecordMembership(System.currentTimeMillis());
        if (runningVolumeSum < MIN_VOLUME_THRESHOLD && runningVolumeSum > 0) {
            log.info("SUSPICIOUSLY LOW VOLUME");
        }
        return runningProductSum / runningVolumeSum;
    }
}

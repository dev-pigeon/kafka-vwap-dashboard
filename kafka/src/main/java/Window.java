import java.util.ArrayDeque;
import java.util.Deque;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Window {

    private double runningProductSum = 0.0; // The running sum of volume x price for each record
    private double runningVolumeSum = 0.0;
    private final double RECORD_LIFESPAN = 120_000;

    @JsonIgnore
    private Deque<StockRecord> recordDeque;

    // No-args constructor for Jackson
    public Window() {
        this.recordDeque = new ArrayDeque<>();
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
        while (!recordDeque.isEmpty()) {
            StockRecord front = recordDeque.peek();
            long age = now - front.getTimeStamp();
            if (!recordIsValid(front.getTimeStamp(), now)) {
                evictRecord(front);
            } else {
                break;
            }
        }

    }

    public double calculateVWAP() {
        return runningProductSum / runningVolumeSum;
    }
}

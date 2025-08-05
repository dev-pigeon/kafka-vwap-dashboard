package Window;

import java.util.Deque;
import java.util.LinkedList;

import StockRecord.StockRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Window {

    private double runningProductSum = 0.0; // The running sum of volume x price for each record
    private double runningVolumeSum = 0.0;
    private final double RECORD_LIFESPAN = 120_000;
    private final double MIN_VOLUME_THRESHOLD = 1e-6;
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(Window.class);

    private Deque<StockRecord> recordDeque = new LinkedList<>();

    // No-args constructor for Jackson
    public Window() {
    }

    public Deque<StockRecord> getRecords() {
        return recordDeque;
    }

    public int size() {
        return recordDeque.size();
    }

    public double getRunningProductSum() {
        return runningProductSum;
    }

    public double getRunningVolumeSum() {
        return runningVolumeSum;
    }

    public void updateWindow(StockRecord record) {
        double recordVolume = record.getVolume();
        runningProductSum += (record.getClosePrice() * recordVolume);
        runningVolumeSum += recordVolume;
        recordDeque.add(record);
    }

    private boolean recordIsValid(long recordTimeStamp, long now) {
        if ((now - recordTimeStamp) > RECORD_LIFESPAN) {
            return false;
        }
        return true;
    }

    protected void evictRecord(StockRecord record) {
        double recordVolume = record.getVolume();
        runningProductSum -= (record.getClosePrice() * recordVolume);
        runningVolumeSum -= recordVolume;
        recordDeque.pop();
    }

    protected void checkRecordMembership(long now) {
        while (!recordDeque.isEmpty()) {
            StockRecord front = recordDeque.peek();
            if (!recordIsValid(front.getTimeStamp(), now)) {
                evictRecord(front);
            } else {
                break;
            }
        }
    }

    private boolean validSums() {
        return runningVolumeSum >= MIN_VOLUME_THRESHOLD && runningProductSum > 0;
    }

    private boolean isValidVWAP(double vwap) {
        return vwap != Double.POSITIVE_INFINITY && vwap != Double.NEGATIVE_INFINITY;
    }

    public Double calculateVWAP() {
        checkRecordMembership(System.currentTimeMillis());
        if (!(validSums())) {
            return null;
        }

        final double windowVwap = runningProductSum / runningVolumeSum;
        if (!isValidVWAP(windowVwap)) {
            return null;
        }

        return windowVwap;
    }
}

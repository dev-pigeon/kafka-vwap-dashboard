import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;
import java.util.ArrayList;

class WindowTest {

    private static ArrayList<StockRecord> records = new ArrayList<>();
    private static Random random = new Random();

    @BeforeAll
    static void initialize() {
        final String ticker = "AAPL";
        final int min = 1;
        final int max = 100;
        for (int i = 0; i < 5; ++i) {
            final double price = random.nextDouble(max - min + 1) + min;
            final double volume = random.nextDouble(max - min + 1) + min;
            StockRecord record = new StockRecord(ticker, price, volume, System.currentTimeMillis());
            records.add(record);
        }
    }

    @Test
    void canInitialize() {
        Window window = new Window();
        assertNotNull(window);
    }

    @Test
    void properlyAccumulatesSums() {
        Window window = new Window();
        double currVolumeSum = 0;
        double currProductSum = 0;
        for (StockRecord record : records) {
            window.updateWindow(record);
            currVolumeSum += record.getVolume();
            currProductSum += (record.getClosePrice() * record.getVolume());
            assertEquals(currVolumeSum, window.getRunningVolumeSum());
            assertEquals(currProductSum, window.getRunningProductSum());
        }
    }

    @Test
    void properlyEvictsRecords() {
        Window window = new Window();
        for (StockRecord record : records) {
            window.updateWindow(record);
        }

        final int randIndex = random.nextInt(records.size() + 1);
        StockRecord toRemove = records.get(randIndex);
        final double expectedVolume = window.getRunningVolumeSum() - toRemove.getVolume();
        final double expectedProduct = window.getRunningProductSum()
                - (toRemove.getClosePrice() * toRemove.getVolume());
        final int expectedSize = window.size() - 1;
        window.evictRecord(toRemove);

        assertEquals(expectedVolume, window.getRunningVolumeSum());
        assertEquals(expectedProduct, window.getRunningProductSum());
        assertEquals(expectedSize, window.size());
    }

}
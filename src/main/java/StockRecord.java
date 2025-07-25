import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockRecord {
    private String ticker;
    private double closePrice;
    private double volume;
    private long timeStamp;

    @JsonCreator
    public StockRecord(
            @JsonProperty("ticker") String ticker,
            @JsonProperty("closePrice") double price,
            @JsonProperty("volume") double volume,
            @JsonProperty("timestamp") long timestamp) {
        this.ticker = ticker;
        this.closePrice = price;
        this.volume = volume;
        this.timeStamp = timestamp;
    }

    public String getTicker() {
        return this.ticker;
    }

    public double getClosePrice() {
        return this.closePrice;
    }

    public double getVolume() {
        return this.volume;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public String toString() {
        return String.format("Ticker:  %s Price: %f  Volume: %f  Timestamp: %f", this.ticker, this.closePrice,
                this.volume, this.timeStamp);
    }
}

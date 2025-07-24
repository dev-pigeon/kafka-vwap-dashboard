import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockRecord {
    private String ticker;
    private double closePrice;
    private double volume;

    @JsonCreator
    public StockRecord(
            @JsonProperty("ticker") String ticker,
            @JsonProperty("closePrice") double price,
            @JsonProperty("volume") double volume) {
        this.ticker = ticker;
        this.closePrice = price;
        this.volume = volume;
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

    public String toString() {
        return String.format("Ticker: %s   Price: %f  Volume: %f", this.ticker, this.closePrice, this.volume);
    }
}

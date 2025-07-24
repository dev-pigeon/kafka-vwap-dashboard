public class StockRecord {
    private String ticker;
    private double closePrice;
    private double volume;

    public StockRecord(String ticker, double price, double volume) {
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
        return String.format("Ticker: %s   Price: %f  Volume: $f", this.ticker, this.closePrice, this.volume);
    }
}

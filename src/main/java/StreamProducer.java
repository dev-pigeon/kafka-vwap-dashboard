import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.kafka.clients.producer.*;

public class StreamProducer {
    private static Long rate = null;

    public static void main(String[] args) {
        if (args.length > 0) {
            String rate_string = args[0];
            rate = Long.parseLong(rate_string);
        }
        streamFile();
    }

    private static void streamFile() {
        String filePath = "./data/stock_data_cleaned.csv";
        System.out.println("Opening file...");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // dispose of first line
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                Thread.sleep(rate);
            }
        } catch (Exception e) {

        }
    }
}

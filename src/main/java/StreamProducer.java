import org.apache.kafka.clients.producer.*;

public class StreamProducer {
    private static double rate;

    public static void main(String[] args) {
        if (args.length > 0) {
            String rate_string = args[0];
            rate = Double.parseDouble(rate_string);
        }
    }
}

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WindowTest {

    @Test
    void canInitialize() {
        Window window = new Window();
        assertNotNull(window);
    }
}
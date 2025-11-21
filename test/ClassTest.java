import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestingClassTest {
    TestingClass tc = new TestingClass();

    @Test
    void returnIntTest() {
        assertEquals(1, tc.returnInt());
    }
}
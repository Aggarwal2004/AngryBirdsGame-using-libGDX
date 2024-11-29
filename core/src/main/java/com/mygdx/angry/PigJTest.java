// Java
import com.mygdx.angry.PigJ;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PigJTest {
    private PigJ pig;

    @Before
    public void setUp() {
        pig = new PigJ(100);
    }

    @Test
    public void testTakeDamage() {
        pig.takeDamage(50);
        assertEquals("Health should be reduced by 50", 50, pig.getHealth());

        pig.takeDamage(60);
        assertEquals("Health should not go below 0", -10, pig.getHealth());
        assertTrue("Pig should be destroyed when health is 0 or less", pig.isDestroyed());
    }

    @Test
    public void testTakeDamageWhenDestroyed() {
        pig.takeDamage(100);
        assertTrue("Pig should be destroyed", pig.isDestroyed());

        pig.takeDamage(10);
        assertEquals("Health should remain 0 after being destroyed", 0, pig.getHealth());
    }
}

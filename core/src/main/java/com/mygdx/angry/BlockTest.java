import com.mygdx.angry.BlockJ;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    private BlockJ block;

    @Before
    public void setUp() {
        block = new BlockJ(100);
    }

    @Test
    public void testTakeDamage() {
        block.takeDamage(50);
        assertEquals("Health should be reduced by 50", 50, block.getHealth());

        block.takeDamage(60);
        assertEquals("Health should not go below 0", -10, block.getHealth());
        assertTrue("Block should be destroyed when health is 0 or less", block.isDestroyed());
    }

    @Test
    public void testTakeDamageWhenDestroyed() {
        block.takeDamage(100);
        assertTrue("Block should be destroyed", block.isDestroyed());

        block.takeDamage(10);
        assertEquals("Health should remain 0 after being destroyed", 0, block.getHealth());
    }
}

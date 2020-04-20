import org.junit.Test;
import static org.junit.Assert.fail;

public class DocDaneeka {
    @Test
    public void evaluate() throws Throwable {
        Yossarian yossarian = Catch22.loophole();
        if (! yossarian.isCrazy()) fail("Keep fighting!");
    }
}
import org.junit.Test;
import statsVisualiser.gui.MainUI;

import static junit.framework.TestCase.fail;


public class FrameTest {

    @Test
    public void getMainUISuccess() throws Exception {
        try {
            MainUI.getInstance();
        } catch (Exception exception) {
            fail("Application frame did not render successfully.");
        }
    }
}

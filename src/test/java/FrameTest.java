import org.junit.Test;
import statsVisualiser.gui.MainUI;

import static junit.framework.TestCase.*;


public class FrameTest {

    @Test
    public void getMainUISuccess() throws Exception {
        System.out.println("Tests if the application frame is returned successfully");

        try {
            assertEquals(MainUI.getInstance().getClass().toString(),
                    "class statsVisualiser.gui.MainUI");
            assertNotNull(MainUI.getInstance());
        } catch (Exception exception) {
            fail("Application frame did not render successfully.");
        }
    }
}

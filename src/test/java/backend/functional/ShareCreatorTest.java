package backend.functional;

import backend.creators.ShareCreator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShareCreatorTest {

    @Test
    public void doesShareCreationWork() {
        assertEquals(ShareCreator.getNameList(10)[0], new String[0]);
    }
}

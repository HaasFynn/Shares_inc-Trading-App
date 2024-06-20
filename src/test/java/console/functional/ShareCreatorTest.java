package console.functional;

import console.creators.ShareCreator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShareCreatorTest {

    @Test
    public void doesShareCreationWork() {
        assertEquals(ShareCreator.getNameList(10)[0], new String[0]);
    }
}

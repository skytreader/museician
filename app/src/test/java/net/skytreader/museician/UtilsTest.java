package net.skytreader.museician;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by chad on 6/10/17.
 */

public class UtilsTest {

    @Test
    public void testExtractFilename(){
        String[] fakePath = {"", "home", "chad", "kode", "test.py"};
        assertEquals(fakePath[fakePath.length - 1], Utils.extractFilename
                (fakePath));
    }
}

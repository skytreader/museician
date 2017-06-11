package net.skytreader.museician;

import android.content.SharedPreferences;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by chad on 6/10/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class KVStoreTest {

    @Mock
    private SharedPreferences mockSp;

    private final String NONEXISTENT_KEY = "NONEXISTENT_KEY";

    @Test
    public void testKVStore(){
        KVStore kvs = createMockedKVStore();

        String nonexistent = kvs.get("NONEXISTENT_KEY", null);
        assertEquals(null, nonexistent);
        kvs.set("NONEXISTENT_KEY", "not anymore!");
        assertEquals("not anymore!", kvs.get("NONEXISTENT_KEY", null));
        kvs.set("NONEXISTENT_KEY", "NOT ANYMORE!");
        assertEquals("NOT ANYMORE!", kvs.get("NONEXISTENT_KEY", null));
    }

    private KVStore createMockedKVStore(){
        when(mockSp.getString(NONEXISTENT_KEY, null)).thenReturn("not " +
                "anymore!");
        return new KVStore(mockSp);
    }

}

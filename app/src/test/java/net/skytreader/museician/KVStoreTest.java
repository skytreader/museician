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

    @Mock
    private SharedPreferences.Editor mockSpEditor;

    private final String NONEXISTENT_KEY = "NONEXISTENT_KEY";

    @Test
    public void testKVStore(){
        KVStore kvs = createMockedKVStore();

//        String nonexistent = kvs.get("NONEXISTENT_KEY", null);
//        assertEquals(null, nonexistent);
//        kvs.set("NONEXISTENT_KEY", "not anymore!");
//        assertEquals("not anymore!", kvs.get("NONEXISTENT_KEY", null));
//        kvs.set("NONEXISTENT_KEY", "NOT ANYMORE!");
//        assertEquals("NOT ANYMORE!", kvs.get("NONEXISTENT_KEY", null));
        assertEquals(kvs.get("test", null), "yes");
    }

    private KVStore createMockedKVStore(){
        //when(mockSp.getString(NONEXISTENT_KEY, null)).thenReturn("not " +
        //        "anymore!");
        mockSp = mock(SharedPreferences.class);
        mockSpEditor = mock(SharedPreferences.Editor.class);
        when(mockSpEditor.commit()).thenReturn(true);
        when(mockSp.edit()).thenReturn(mockSpEditor);
        when(mockSp.getString("test", null)).thenReturn("yes");
        //when(mockSp.getString(NONEXISTENT_KEY, null)).thenCallRealMethod();
        return new KVStore(mockSp);
    }

}

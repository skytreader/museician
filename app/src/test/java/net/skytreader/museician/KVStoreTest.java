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

        String nonexistent = kvs.get(NONEXISTENT_KEY, null);
        assertEquals(null, nonexistent);
        when(mockSp.getString(NONEXISTENT_KEY, null)).thenReturn("not " +
                "anymore!");
        assertEquals("not anymore!", kvs.get(NONEXISTENT_KEY, null));
        when(mockSp.getString(NONEXISTENT_KEY, null)).thenReturn("NOT " +
                "ANYMORE!");
        assertEquals("NOT ANYMORE!", kvs.get(NONEXISTENT_KEY, null));
    }

    private KVStore createMockedKVStore(){
        mockSp = mock(SharedPreferences.class);
        mockSpEditor = mock(SharedPreferences.Editor.class);
        when(mockSpEditor.commit()).thenReturn(true);
        when(mockSp.edit()).thenReturn(mockSpEditor);
        return new KVStore(mockSp);
    }

}

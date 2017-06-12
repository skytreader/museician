package net.skytreader.museician;

import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by chad on 6/12/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class LRUPriorityQueueTest {

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mSharedPrefEditor;

    private final String TEST_NAMESPACE = "TEST_NAMESPACE";
    private final String[] RIGGED_QUEUE = {
            "Too Much Information", "Ordinary World", "Love Voodoo",
            "Drowning Man", "Shotgun", "Come Undone", "Breath After Breath",
            "UMF"
    };

    private void initMockedSharedPreferences() {
        mSharedPref = mock(SharedPreferences.class);
        mSharedPrefEditor = mock(SharedPreferences.Editor.class);
        when(mSharedPref.edit()).thenReturn(mSharedPrefEditor);
        when(mSharedPrefEditor.commit()).thenReturn(true);
    }

    /**
     * This method assumes that you are inserting the elements of
     * RIGGED_QUEUE from index 0 onwards which would result to a queue where
     * the least-recently used is index 0. This will only build the first
     * full instance of the queue, and will not take into account any evictions.
     *
     * @param queueLen
     * @return
     */
    private String[] buildQueueRepr(int queueLen) {
        LinkedList<String> riggedQueueSlice = (LinkedList<String>) Arrays.asList(Arrays.copyOf
                (RIGGED_QUEUE, queueLen));
        Collections.reverse(riggedQueueSlice);
        return (String[]) riggedQueueSlice.toArray();
    }

    @Test
    public void testNewLRUPriorityQueue() {
        int testQueueLen = 2;
        initMockedSharedPreferences();
        LRUPriorityQueue lpq = new LRUPriorityQueue(mSharedPref, testQueueLen,
                TEST_NAMESPACE);

        assertEquals(0, lpq.size());

        for (int i = 0; i < testQueueLen; i++) {
            lpq.enqueue(RIGGED_QUEUE[i]);
            when(mSharedPref.getString(eq(lpq.buildKeyForm(i)), anyString()))
                    .thenReturn(RIGGED_QUEUE[i]);
        }

        assertEquals(testQueueLen, lpq.size());
        String[] qRepr = buildQueueRepr(testQueueLen);
        String[] actualContents = lpq.getContents();
        assertArrayEquals(qRepr, actualContents);
    }

}

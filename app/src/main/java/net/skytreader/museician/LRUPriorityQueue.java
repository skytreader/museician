package net.skytreader.museician;

import android.content.SharedPreferences;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Implements a priority queue that evicts members based on recency of access.
 * The underlying storage for the queue items is a namespaced SharedPreferences
 * instance.
 *
 * Would have used {@link android.util.LruCache} but then it does not seem to
 * have some of the functionality I need so this.
 *
 * WARNING: Not at all efficient for very large sizes. The intended use case
 * is here is for up to 4 items, with very little of actual app usage time
 * touching this code so this should be acceptable.
 *
 * Created by chad on 6/11/17.
 */

public class LRUPriorityQueue {

    private SharedPreferences spStorage;
    private SharedPreferences.Editor spStorageEditor;
    private String keyNamespace;
    private Deque<String> representation;

    public LRUPriorityQueue(SharedPreferences sp, int queueLen, String
            namespace){
        spStorage = sp;
        spStorageEditor = sp.edit();
        representation = new LinkedList<String>();
        keyNamespace = namespace;
    }

    public int getSize(){
        return representation.length;
    }

    public void enqueue(String val){

    }
}

package net.skytreader.museician;

import android.content.SharedPreferences;

import java.lang.reflect.Array;
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

    private KVStore kvStore;
    private String keyNamespace;
    private LinkedList<String> representation;
    private int lenLimit;

    public LRUPriorityQueue(SharedPreferences sp, int queueLen, String
            namespace){
        kvStore = new KVStore(sp);
        representation = new LinkedList<>();
        keyNamespace = namespace;
        lenLimit = queueLen;
        initialize();
    }

    /**
     * This is protected so that unit tests may use it.
     *
     * @param index
     * @return
     */
    protected String buildKeyForm(int index){
        return keyNamespace + "_" + Integer.toString(index);
    }

    public void initialize(){
        int i = 0;
        String elementI = kvStore.get(buildKeyForm(i), null);

        while(elementI != null){
            representation.addLast(elementI);
            i++;
            elementI = kvStore.get(buildKeyForm(i), null);
        }
    }

    public int size(){
        return representation.size();
    }

    public void enqueue(String val){
        int nextIndex = representation.size();

        if(nextIndex == lenLimit){
            representation.removeLast();
        }
        representation.addFirst(val);
        int limit = representation.size();

        for(int i = 0; i < limit; i++){
            kvStore.set(buildKeyForm(i), representation.get(i));
        }
    }

    private String[] getContents(){
        return Array.newInstance(representation, representation.size());
    }
}

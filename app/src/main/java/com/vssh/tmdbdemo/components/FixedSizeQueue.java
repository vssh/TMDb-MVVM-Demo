package com.vssh.tmdbdemo.components;

import java.util.ArrayList;

/**
 * Class that implements a max-size queue.
 * Any additions after max size is reached will delete the earliest items.
 */

public class FixedSizeQueue<T> extends ArrayList<T> {
    private int size;

    public FixedSizeQueue(int size) {
        super();
        this.size = size;
    }

    @Override
    public boolean add(T o) {
        boolean res = false;
        if (!contains(o)) {
            res = super.add(o);
        }
        if (size() > size){
            removeRange(0, size() - size);
        }
        return res;
    }
}

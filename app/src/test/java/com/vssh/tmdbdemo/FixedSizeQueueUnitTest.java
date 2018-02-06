package com.vssh.tmdbdemo;

import com.vssh.tmdbdemo.components.FixedSizeQueue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test to check FixedSizeQueue implementation
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class FixedSizeQueueUnitTest {
    @Test
    public void fixedSizeQueueSize_isCorrect() throws Exception {
        FixedSizeQueue<Integer> intQueue = new FixedSizeQueue<>(3);

        intQueue.add(1);
        intQueue.add(2);
        intQueue.add(3);
        intQueue.add(4);
        intQueue.add(5);
        intQueue.add(6);

        assertEquals(3, intQueue.size());
        assertEquals(4, intQueue.get(0).intValue());
    }
}
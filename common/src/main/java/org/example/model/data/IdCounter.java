package org.example.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

public class IdCounter {
    @Setter
    @Getter
    private static AtomicLong nextId;

    static {
        nextId = new AtomicLong(0);
    }

    public static long getNextIdAndIncrement() {
        return nextId.incrementAndGet();
    }
}

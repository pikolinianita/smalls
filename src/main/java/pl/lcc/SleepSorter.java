package pl.lcc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class SleepSorter <T>{

    List<T> sort (List<? extends T> source, Function<T, Integer> value){
        ConcurrentLinkedQueue<T> result = new ConcurrentLinkedQueue<>();
        var threadBuilder = Thread.ofVirtual();
        threadBuilder.name("Virtual");
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(source.size());

        source.forEach(element -> threadBuilder.start(new Sleeper(value.apply(element), () -> result.add(element), start, finish)));
        start.countDown();
        try {
           finish.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(result);
    }

}

class Sleeper implements Runnable {

    private final CountDownLatch start;
    private final CountDownLatch finish;
    int time;
    Callback callback;

    int multi = 5;

    public Sleeper(int time, Callback callback, CountDownLatch start, CountDownLatch finish) {
        this.time = time;
        this.callback = callback;
        this.start = start;
        this.finish = finish;
    }

    @Override
    public void run() {
        try {
            start.await();
            Thread.sleep(time*multi);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        callback.call();
        finish.countDown();
    }

    public void setMulti(int multi) {
        this.multi = multi;
    }
}

@FunctionalInterface
interface Callback {
    void call();
}
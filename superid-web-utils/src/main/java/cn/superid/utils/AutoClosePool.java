package cn.superid.utils;

import cn.superid.utils.functions.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * pool with auto-close feature
 * objects put into this pool will auto close and removed in timeout
 * Created by zoowii on 2014/9/19.
 */
public class AutoClosePool<K, T> {
    private static final Logger LOG = Logger.getLogger(AutoClosePool.class);
    protected final int timeoutMilliSeconds;
    protected final Function<T, Void> closeFn;
    protected final ConcurrentMap<K, Pair<T, Date>> poolObjects = new ConcurrentHashMap<K, Pair<T, Date>>();
    protected ReentrantLock closeLock = new ReentrantLock();
    protected boolean stopped = false;
    protected static final Executor executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public void stop() {
        stopped = true;
    }

    public boolean isRunning() {
        return !stopped;
    }

    protected class CheckClosedRunnable implements Runnable {
        private final AutoClosePool<K, T> pool;

        public CheckClosedRunnable(AutoClosePool<K, T> pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            while (pool.isRunning()) {
                pool.checkClosed();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error(e);
                }
            }
        }
    }

    public AutoClosePool(int timeoutMilliSeconds, Function<T, Void> closeFn) {
        this.timeoutMilliSeconds = timeoutMilliSeconds;
        this.closeFn = closeFn;
        if (closeFn != null) {
            executor.execute(new CheckClosedRunnable(this));
        }
    }

    public void put(K key, T obj) {
        if (key != null) {
            poolObjects.put(key, Pair.of(obj, new Date()));
        }
    }

    public T remove(K key) {
        if (key != null) {
            Pair<T, Date> pair = poolObjects.remove(key);
            return pair != null ? pair.getLeft() : null;
        } else {
            return null;
        }
    }

    protected boolean isAvailablePair(Pair<T, Date> pair) {
        if (pair == null) {
            return false;
        }
        Date date = pair.getRight();
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, timeoutMilliSeconds);
        return calendar.getTime().after(new Date()) && pair.getLeft() != null;
    }

    public T get(K key) {
        Pair<T, Date> pair = poolObjects.get(key);
        if (pair == null) {
            return null;
        }
        return isAvailablePair(pair) ? pair.getLeft() : null;
    }

    /**
     * check and close objects in this pool who are timeout
     */
    public void checkClosed() {
        for (K key : poolObjects.keySet()) {
            if (key == null) {
                continue;
            }
            Pair<T, Date> pair = poolObjects.get(key);
            if (!isAvailablePair(pair)) {
                if (!closeLock.tryLock()) {
                    continue;
                }
                try {
                    T obj = pair.getLeft();
                    if (closeFn != null) {
                        closeFn.apply(obj);
                    }
                    poolObjects.remove(key);
                } catch (Exception e) {
                    LOG.error(e);
                } finally {
                    closeLock.unlock();
                }
            }
        }
    }
}

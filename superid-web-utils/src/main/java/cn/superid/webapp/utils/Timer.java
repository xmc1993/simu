package cn.superid.webapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by zoowii on 14/10/13.
 */
public class Timer {
    private static final Logger LOG = LoggerFactory.getLogger(Timer.class);
    private Date startTime = new Date();
    private Date endTime = null;

    public Timer() {
    }

    public Timer(boolean start) {
        if (start) {
            start();
        }
    }

    public void start() {
        startTime = new Date();
    }

    public void end() {
        endTime = new Date();
        log();
    }

    public void log() {
        if (endTime == null || endTime.before(startTime)) {
            LOG.info("you need end the timer");
            return;
        }
        System.out.println(String.format("using %dms, start at %s, end at %s", getTime(), startTime.toString(), endTime.toString()));
    }

    public long getTime() {
        assert endTime != null && endTime.after(startTime);
        return endTime.getTime() - startTime.getTime();
    }
}

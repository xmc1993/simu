package util;
import cn.superid.jpa.util.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Execution;

import java.util.Date;

/**
 * Created by xiaofengxu on 16/9/8.
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
        System.out.println(String.format("using %dms", getTime()));
    }

    public long getTime() {
        assert endTime != null && endTime.after(startTime);
        return endTime.getTime() - startTime.getTime();
    }

    public  void executeRepeate(Function function,int times){
        for(int i=0;i<times;i++){
            function.apply(null);
        }
    }


    public static void  compair( int times,Execution... executions){
        for(Execution execution:executions){
            Timer timer = new Timer();
            for(int i=0;i<times;i++){
                execution.execute();
            }
            timer.end();
        }

    }
}

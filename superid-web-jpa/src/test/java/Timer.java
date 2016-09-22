import cn.superid.jpa.util.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        System.out.println(String.format("using %dms, start at %s, end at %s", getTime(), startTime.toString(), endTime.toString()));
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


    public static void  compair(Execution execution1,Execution execution2,int times){
        Timer timer = new Timer();
        for(int i=0;i<times;i++){
            execution1.execute();
        }
        timer.end();

        Timer timer1 = new Timer();
        for(int i=0;i<times;i++){
            execution2.execute();
        }
        timer1.end();

    }
}

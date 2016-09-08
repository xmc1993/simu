package cn.superid.webapp.tasks;

import org.springframework.context.annotation.Bean;

/**
 * Created by xiaofengxu on 16/9/7.
 */

public class RunningTests {
    private int running =0;
    public static Long userId=0L;

    @Bean
    public  int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }
}

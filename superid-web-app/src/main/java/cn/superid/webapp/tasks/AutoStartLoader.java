package cn.superid.webapp.tasks;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AutoStartLoader {
    private static final Logger LOG = LoggerFactory.getLogger(AutoStartLoader.class);
    private int version;
    private int tradeOutTime = 7;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * 获取上一次初始化完成时的本webapp的版本号
     *
     * @return
     */


//    private class checkTradeTimeOut extends TimerTask{
//        @Override
//        public void run() {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-tradeOutTime);//7天前
//            SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//            TradingEntity.getSession().executeNativeSql("update trading t set t.is_timeout = 1 where t.create_time<? and t.state =0",new ParameterBindings(sdf.format(calendar.getTime())));
//        }
//    }
    /**
     * TODO
     * 初始化root用户,root盟,审核事务等系统信息,并写入系统配置表中
     */
    public void init() {
        LOG.info("system starting");
//
//        Timer timer = new Timer();
//        timer.schedule(new checkTradeTimeOut(), 0, 3 * 60 * 1000);
    }
}

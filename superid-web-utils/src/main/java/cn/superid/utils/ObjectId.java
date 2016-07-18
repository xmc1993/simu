package cn.superid.utils;

/**
 * |0|1|2|3|4|5|6|7|8|9|10|11|
 * time: [0,3]
 * machineId: [4,6]
 * pid: [7]
 * inc: [8,11]
 * Created by zoowii on 2014/9/9.
 */
public class ObjectId implements Comparable<ObjectId>, java.io.Serializable {
    private final int time;
    private final int machineId;
    private final int inc;
    private boolean _new;
    private int pid;
    private static final IncrementCircleNumber incrementCircleNumber = new IncrementCircleNumber(0, 256 * 256 * 256);

    public ObjectId(int machineId, int pid) {
        this.time = (int) (System.currentTimeMillis() / 1000);
        this.machineId = machineId;
        this.inc = (int) incrementCircleNumber.getAndIncrement();
        // 使用一个每个id generator唯一的pid,不要使用进程ID,进程ID可能一样. 改成从配置文件中读取,或者从一个registry服务中申请一个唯一ID(同时使用中的这个ID唯一,所以可能需要释放ID, 心跳包机制等)
        this.pid = pid;
        this._new = true;
    }

    @Override
    public int compareTo(ObjectId o) {
        if (o == null) {
            return 1;
        }
        if (this.time != o.time) {
            return this.time - o.time;
        }
        if (this.machineId != o.machineId) {
            return this.machineId - o.machineId;
        }
        if (this.pid != o.pid) {
            return this.pid - o.pid;
        }
        return this.inc - o.inc;
    }

    private static String repeatString(String unit, int n) {
        if (n <= 0 || StringUtil.isEmpty(unit)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            builder.append(unit);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        String timePart = Integer.toHexString(time);
        if (timePart.length() < 8) {
            timePart = repeatString("0", 8 - timePart.length()) + timePart;
        }
        String machinePart = Integer.toHexString(machineId);
        if (machinePart.length() > 6) {
            machinePart = machinePart.substring(machinePart.length() - 6);
        } else {
            machinePart = repeatString("0", 6 - machinePart.length()) + machinePart;
        }
        String pidPart = Integer.toHexString(pid);
        if (pidPart.length() > 4) {
            pidPart = pidPart.substring(pidPart.length() - 4);
        } else {
            pidPart = repeatString("0", 4 - pidPart.length()) + pidPart;
        }
        String incPart = Integer.toHexString(inc);
        if (incPart.length() > 6) {
            incPart = incPart.substring(incPart.length() - 6);
        } else {
            incPart = repeatString("0", 6 - incPart.length()) + incPart;
        }
        return timePart + machinePart + pidPart + incPart;
    }
}

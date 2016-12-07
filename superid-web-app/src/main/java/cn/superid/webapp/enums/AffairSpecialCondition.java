package cn.superid.webapp.enums;

/**
 * Created by njuTms on 16/10/17.
 * 用于在进行事务操作时对事务的检测,现有 有子事务,有交易,以后可能会有 有合同等
 */
public class AffairSpecialCondition {
    public final static int NO_SPECIAL = 0;
    public final static int HAS_CHILD = 1;
    public final static int HAS_TRADE = 2;
}

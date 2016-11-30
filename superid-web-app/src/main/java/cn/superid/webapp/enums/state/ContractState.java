package cn.superid.webapp.enums.state;

/**
 * Created by njuTms on 16/11/30.
 * 用来处理合同的各阶段状态
 */
public class ContractState {
    public static int Invalid = 0; //失效
    public static int Starting = 1; //正在发起
    public static int NotConfirmed = 2; //发起成功未确认
    public static int Valid = 3; //已确认正在生效
    public static int Terminating = 4; //正在终止
}

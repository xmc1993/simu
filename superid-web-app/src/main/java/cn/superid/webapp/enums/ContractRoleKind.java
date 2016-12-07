package cn.superid.webapp.enums;

/**
 * Created by jizhneya on 16/9/20.
 * 用于表示合同中的甲乙方
 */
public enum ContractRoleKind {
    JIAFANG("甲方",1),YIFANG("乙方",0),BINGFANG("丙方",2),DINGFANG("丁方",3),
    WUFANG("戊方",4),JIFANG("己方",5),GENGFANG("庚方",6),XINFANG("辛方",7),RENFANG("壬方",8),
    GUIFANG("癸方",9),OTHER("其他方",10);

    private String kind;
    private int index;

    private ContractRoleKind(String kind , int index){
        this.index = index;
        this.kind = kind;
    }

    @Override
    public String toString(){
        return this.kind;
    }

    public int toInt(){
        return this.index;
    }
}

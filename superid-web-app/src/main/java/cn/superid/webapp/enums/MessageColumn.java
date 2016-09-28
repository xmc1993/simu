package cn.superid.webapp.enums;

/**
 * Created by njuTms on 16/9/28.
 */
public enum MessageColumn {
    TYPE("type"),
    ISREAD("isRead");
    private String columnName;
    MessageColumn(String columnName){
        this.columnName = columnName;
    }

    @Override
    public String toString(){
        return this.columnName;
    }
}

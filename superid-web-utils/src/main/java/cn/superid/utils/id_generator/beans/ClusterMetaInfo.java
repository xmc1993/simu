package cn.superid.utils.id_generator.beans;

import java.util.List;

/**
 * 数据库集群元信息
 * Created by zoowii on 2014/9/2.
 */
public class ClusterMetaInfo {
    private List<ServerGroup> groups;
    private List<ServerMigration> migrations;
    private List<DbTableInfo> tables;

    public List<ServerGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ServerGroup> groups) {
        this.groups = groups;
    }

    public List<ServerMigration> getMigrations() {
        return migrations;
    }

    public void setMigrations(List<ServerMigration> migrations) {
        this.migrations = migrations;
    }

    public List<DbTableInfo> getTables() {
        return tables;
    }

    public void setTables(List<DbTableInfo> tables) {
        this.tables = tables;
    }
}

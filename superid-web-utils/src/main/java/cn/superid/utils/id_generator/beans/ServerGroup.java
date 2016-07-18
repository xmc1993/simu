package cn.superid.utils.id_generator.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoowii on 2014/8/29.
 */
public class ServerGroup {
    private String name;
    private List<ServerState> servers = new ArrayList<ServerState>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServerState> getServers() {
        return servers;
    }

    public void setServers(List<ServerState> servers) {
        this.servers = servers;
    }
}

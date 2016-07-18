package cn.superid.webapp.utils;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * elasticsearch client integrated with spring
 */
public class ESFactory {
    private static Client client;

    private ESFactory() {
    }

    static {
        Node node = NodeBuilder.nodeBuilder().client(true).node();
        ESFactory.client = node.client();
    }

    public static Client getClient() {
        return ESFactory.client;
    }
}

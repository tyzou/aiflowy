package tech.aiflowy.ai.tinyflow.listener;

import com.alicp.jetcache.Cache;
import dev.tinyflow.core.chain.Chain;
import dev.tinyflow.core.chain.Node;
import dev.tinyflow.core.chain.listener.NodeErrorListener;

import java.util.Map;

public class NodeErrorListenerForFront implements NodeErrorListener {

    private final Cache<String, Object> cache;

    public NodeErrorListenerForFront(Cache<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public void onError(Throwable error, Node node, Map<String, Object> nodeResult, Chain chain) {
        String executeId = chain.getStateInstanceId();
        System.out.println("node error: " + error.getMessage() + ": " + node.getName());
    }
}

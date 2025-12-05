package tech.aiflowy.ai.tinyflow.listener;

import com.alicp.jetcache.Cache;
import dev.tinyflow.core.chain.Chain;
import dev.tinyflow.core.chain.listener.ChainErrorListener;

public class ChainErrorListenerForFront implements ChainErrorListener {

    private final Cache<String, Object> cache;

    public ChainErrorListenerForFront(Cache<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public void onError(Throwable error, Chain chain) {
        String executeId = chain.getStateInstanceId();
        System.out.println("ChainErrorListenerForFront: " + error.getMessage());
    }
}

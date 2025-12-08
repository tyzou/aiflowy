package tech.aiflowy.ai.tinyflow.listener;

import com.alicp.jetcache.Cache;
import dev.tinyflow.core.chain.*;
import dev.tinyflow.core.chain.event.*;
import dev.tinyflow.core.chain.listener.ChainEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aiflowy.ai.tinyflow.entity.ChainInfo;
import tech.aiflowy.ai.tinyflow.entity.NodeInfo;
import tech.aiflowy.common.constant.CacheKey;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChainEventListenerForFront implements ChainEventListener {


    private static final Logger log = LoggerFactory.getLogger(ChainEventListenerForFront.class);
    private final Cache<String, Object> cache;

    public ChainEventListenerForFront(Cache<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public void onEvent(Event event, Chain chain) {
        if (event instanceof ChainStartEvent) {
            handleChainStartEvent((ChainStartEvent) event, chain);
        }
        if (event instanceof ChainEndEvent) {
            handleChainEndEvent((ChainEndEvent) event, chain);
        }
        if (event instanceof NodeStartEvent) {
            handleNodeStartEvent((NodeStartEvent) event, chain);
        }
        if (event instanceof NodeEndEvent) {
            handleNodeEndEvent((NodeEndEvent) event, chain);
        }
        if (event instanceof ChainStatusChangeEvent) {
            handleChainStatusChangeEvent((ChainStatusChangeEvent) event, chain);
        }
        if (event instanceof ChainResumeEvent) {
            handleChainResumeEvent((ChainResumeEvent) event, chain);
        }
    }

    private void handleChainStartEvent(ChainStartEvent event, Chain chain) {
        log.info("ChainStartEvent: {}", event);
        String executeId = chain.getStateInstanceId();
        Object status = cache.get(CacheKey.CHAIN_STATUS_CACHE_KEY + executeId);
        if (status == null) {
            status = new ChainInfo();
            ((ChainInfo) status).setExecuteId(executeId);
            ((ChainInfo) status).setStatus(chain.getState().getStatus().getValue());
            setChainInfoCache(executeId, (ChainInfo) status);
        }
    }
    private void handleChainEndEvent(ChainEndEvent event, Chain chain) {
        log.info("ChainEndEvent: {}", event);
        String executeId = chain.getStateInstanceId();
        ChainInfo status = (ChainInfo) cache.get(CacheKey.CHAIN_STATUS_CACHE_KEY + executeId);
        if (status == null) {
            log.error("ChainEndEvent: chain not found");
        } else {
            status.setStatus(chain.getState().getStatus().getValue());
            Map<String, Object> result = chain.getState().getExecuteResult();
            status.setResult(result);
            status.setMessage(chain.getState().getMessage());
            setChainInfoCache(executeId, status);
        }
    }
    private void handleNodeStartEvent(NodeStartEvent event, Chain chain) {
        log.info("NodeStartEvent: {}", event);
        String executeId = chain.getStateInstanceId();
        ChainInfo status = (ChainInfo) cache.get(CacheKey.CHAIN_STATUS_CACHE_KEY + executeId);
        if (status == null) {
            log.error("NodeStartEvent: chain not found");
        } else {
            Node node = event.getNode();
            Map<String, NodeInfo> nodes = status.getNodes();
            NodeInfo nodeInfo = nodes.get(node.getId());
            if (nodeInfo == null) {
                nodeInfo = new NodeInfo();
                nodeInfo.setNodeId(node.getId());
                nodeInfo.setNodeName(node.getName());
                nodeInfo.setStatus(NodeStatus.RUNNING.getValue());
            }
            nodes.put(node.getId(), nodeInfo);
            status.setNodes(nodes);
            setChainInfoCache(executeId, status);
        }
    }
    private void handleNodeEndEvent(NodeEndEvent event, Chain chain) {
        log.info("NodeEndEvent: {}", event);
        String executeId = chain.getStateInstanceId();
        ChainInfo chainInfo = (ChainInfo) cache.get(CacheKey.CHAIN_STATUS_CACHE_KEY + executeId);
        if (chainInfo == null) {
            log.error("NodeEndEvent: chain not found");
        } else {
            Node node = event.getNode();
            NodeState nodeState = chain.getNodeState(node.getId());
            NodeStatus nodeStatus = nodeState.getStatus();
            ExceptionSummary error = nodeState.getError();
            Map<String, NodeInfo> nodes = chainInfo.getNodes();
            NodeInfo nodeInfo = nodes.get(node.getId());
            nodeInfo.setNodeId(node.getId());
            nodeInfo.setStatus(nodeStatus.getValue());
            nodeInfo.setResult(event.getResult());
            nodeInfo.setMessage(error != null ? error.getMessage() : "");
            if (NodeStatus.SUSPEND.equals(nodeStatus)) {
                nodeInfo.setSuspendForParameters(chain.getState().getSuspendForParameters());
            }
            nodes.put(node.getId(), nodeInfo);
            chainInfo.setNodes(nodes);
            setChainInfoCache(executeId, chainInfo);
            System.out.println("node end status -> " + nodeStatus);
        }
    }
    private void handleChainStatusChangeEvent(ChainStatusChangeEvent event, Chain chain) {
        log.info("ChainStatusChangeEvent: {}", event);
        String executeId = chain.getStateInstanceId();
        ChainInfo chainInfo = (ChainInfo) cache.get(CacheKey.CHAIN_STATUS_CACHE_KEY + executeId);
        ChainStatus status = event.getStatus();
        if (ChainStatus.SUSPEND.equals(status)) {
            chainInfo.setStatus(ChainStatus.SUSPEND.getValue());
            setChainInfoCache(executeId, chainInfo);
        }
    }

    private void handleChainResumeEvent(ChainResumeEvent event, Chain chain) {
        log.info("ChainResumeEvent: {}", event);
        String executeId = chain.getStateInstanceId();
    }

    private void setChainInfoCache(String executeId, ChainInfo info) {
        cache.put(CacheKey.CHAIN_STATUS_CACHE_KEY + executeId, info, 3, TimeUnit.DAYS);
    }
}

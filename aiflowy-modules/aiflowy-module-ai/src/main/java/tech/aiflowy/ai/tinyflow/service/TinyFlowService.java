package tech.aiflowy.ai.tinyflow.service;

import dev.tinyflow.core.chain.ChainState;
import dev.tinyflow.core.chain.ExceptionSummary;
import dev.tinyflow.core.chain.NodeState;
import dev.tinyflow.core.chain.NodeStatus;
import dev.tinyflow.core.chain.repository.ChainStateRepository;
import dev.tinyflow.core.chain.repository.NodeStateRepository;
import dev.tinyflow.core.chain.runtime.ChainExecutor;
import org.springframework.stereotype.Component;
import tech.aiflowy.ai.tinyflow.entity.ChainInfo;
import tech.aiflowy.ai.tinyflow.entity.NodeInfo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TinyFlowService {

    @Resource
    private ChainExecutor chainExecutor;

    /**
     * 获取执行状态
     */
    public ChainInfo getChainStatus(String executeId, List<NodeInfo> nodes) {
        // 1. 获取 Repository
        ChainStateRepository chainStateRepository = chainExecutor.getChainStateRepository();
        NodeStateRepository nodeStateRepository = chainExecutor.getNodeStateRepository();

        // 2. 加载主链路状态
        ChainState chainState = chainStateRepository.load(executeId);
        ChainInfo res = getChainInfo(executeId, chainState);

        // 3. 遍历定义的节点，递归查找每个节点的实际状态
        for (NodeInfo node : nodes) {
            processNodeStateRecursively(executeId, node, chainStateRepository, nodeStateRepository);
            res.getNodes().put(node.getNodeId(), node);
        }
        return res;
    }

    /**
     * 递归处理节点状态
     * 逻辑：如果当前层节点是 READY 且存在子链，则深入子链寻找状态；否则使用当前层状态。
     */
    private void processNodeStateRecursively(String currentExecuteId,
                                             NodeInfo node,
                                             ChainStateRepository chainStateRepository,
                                             NodeStateRepository nodeStateRepository) {

        // 加载当前层的状态
        ChainState currentChainState = chainStateRepository.load(currentExecuteId);
        NodeState currentNodeState = nodeStateRepository.load(currentExecuteId, node.getNodeId());

        Set<String> childStateIds = currentChainState.getChildStateIds();

        // 判断是否需要递归下钻
        // 条件：节点状态为 READY (表示当前层只是占位或委托) 且 存在子链
        boolean shouldDiveDeep = NodeStatus.READY.equals(currentNodeState.getStatus())
                && childStateIds != null
                && !childStateIds.isEmpty();

        if (shouldDiveDeep) {
            // 策略：先设置当前状态作为保底（防止子链中没有该节点记录）
            setNodeStatus(node, currentNodeState, currentChainState);

            // 递归遍历所有子链
            // 注意：如果有多个子链（如循环），后遍历的子链状态会覆盖前面的状态，
            // 通常这符合预期（显示最后一次执行的状态）。
            for (String childId : childStateIds) {
                processNodeStateRecursively(childId, node, chainStateRepository, nodeStateRepository);
            }
        } else {
            // 终止递归，直接应用当前层状态
            setNodeStatus(node, currentNodeState, currentChainState);
        }
    }

    private static ChainInfo getChainInfo(String executeId, ChainState chainState) {
        ChainInfo res = new ChainInfo();
        res.setExecuteId(executeId);
        res.setStatus(chainState.getStatus().getValue());
        ExceptionSummary chainError = chainState.getError();
        if (chainError != null) {
            res.setMessage(chainError.getRootCauseClass() + " --> " + chainError.getRootCauseMessage());
        }
        Map<String, Object> executeResult = chainState.getExecuteResult();
        if (executeResult != null && !executeResult.isEmpty()) {
            res.setResult(executeResult);
        }
        return res;
    }

    private void setNodeStatus(NodeInfo node, NodeState nodeState, ChainState chainState) {
        String nodeId = node.getNodeId();
        // 如果状态为空或不存在，可能不需要覆盖，这里视具体业务逻辑而定，目前保持原逻辑
        node.setStatus(nodeState.getStatus().getValue());

        ExceptionSummary error = nodeState.getError();
        if (error != null) {
            node.setMessage(error.getRootCauseClass() + " --> " + error.getRootCauseMessage());
        }

        Map<String, Object> nodeExecuteResult = chainState.getNodeExecuteResult(nodeId);
        if (nodeExecuteResult != null && !nodeExecuteResult.isEmpty()) {
            node.setResult(nodeExecuteResult);
        }

        // 只有当参数不为空时才覆盖
        if (chainState.getSuspendForParameters() != null) {
            node.setSuspendForParameters(chainState.getSuspendForParameters());
        }
    }
}

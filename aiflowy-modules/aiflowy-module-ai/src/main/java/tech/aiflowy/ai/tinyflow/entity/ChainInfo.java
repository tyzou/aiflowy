package tech.aiflowy.ai.tinyflow.entity;

import dev.tinyflow.core.chain.ChainStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ChainInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 执行ID
     */
    private String executeId;
    /**
     * 执行状态
     * @see ChainStatus
     */
    private Integer status;
    /**
     * 消息，错误时显示
     */
    private String message;
    /**
     * 执行结果
     */
    private Map<String, Object> result;
    /**
     * 节点信息
     */
    private Map<String, NodeInfo> nodes = new HashMap<>();

    public String getExecuteId() {
        return executeId;
    }

    public void setExecuteId(String executeId) {
        this.executeId = executeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public Map<String, NodeInfo> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, NodeInfo> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "ChainInfo{" +
                "executeId='" + executeId + '\'' +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", nodes=" + nodes +
                '}';
    }
}

package tech.aiflowy.ai.node;

import com.alibaba.fastjson.JSONObject;
import dev.tinyflow.core.node.BaseNode;
import dev.tinyflow.core.parser.BaseNodeParser;

public class DocNodeParser extends BaseNodeParser {

    @Override
    public BaseNode doParse(JSONObject root, JSONObject data, JSONObject tinyflow) {
        return new DocNode();
    }

    public String getNodeName() {
        return "document-node";
    }
}

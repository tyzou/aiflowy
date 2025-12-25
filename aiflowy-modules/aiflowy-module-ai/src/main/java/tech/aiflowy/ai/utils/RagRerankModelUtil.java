package tech.aiflowy.ai.utils;

import com.agentsflex.rerank.DefaultRerankModel;
import com.agentsflex.rerank.DefaultRerankModelConfig;
import tech.aiflowy.ai.entity.Model;
import tech.aiflowy.common.util.PropertiesUtil;

import java.util.Properties;
import java.util.Map;
import org.springframework.util.StringUtils;

public class RagRerankModelUtil {

    public static DefaultRerankModel getRerankModel(Model modelRerank) {
        if (modelRerank == null){
            return null;
        }
        DefaultRerankModelConfig defaultRerankModelConfig = new DefaultRerankModelConfig();
        if (modelRerank.getModelName() != null && !modelRerank.getModelName().isEmpty()){
            defaultRerankModelConfig.setModel(modelRerank.getModelName());
        }
        if (modelRerank.getApiKey() != null && !modelRerank.getApiKey().isEmpty()){
            defaultRerankModelConfig.setApiKey(modelRerank.getApiKey());
        }
        if (modelRerank.getEndpoint() != null && !modelRerank.getEndpoint().isEmpty()){
            defaultRerankModelConfig.setEndpoint(modelRerank.getEndpoint());
        }
        String llmExtraConfig = modelRerank.getExtraConfig();
        if (llmExtraConfig != null && !llmExtraConfig.isEmpty()){
            Properties prop = PropertiesUtil.textToProperties(llmExtraConfig);
            String basePath = prop.getProperty("basePath");
            defaultRerankModelConfig.setEndpoint(basePath);
        }else{
            Map<String, Object> options = modelRerank.getOptions();
            if (options != null) {
                String rerankPath = (String)options.get("rerankPath");
                if (StringUtils.hasLength(rerankPath)){
                    defaultRerankModelConfig.setRequestPath(rerankPath);
                }
            }
        }

        return new DefaultRerankModel(defaultRerankModelConfig);
    }
}

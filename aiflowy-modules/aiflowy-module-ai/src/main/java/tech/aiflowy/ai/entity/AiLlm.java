
package tech.aiflowy.ai.entity;

import com.agentsflex.llm.deepseek.DeepseekConfig;
import com.agentsflex.llm.deepseek.DeepseekLlm;
import tech.aiflowy.ai.entity.base.AiLlmBase;
import tech.aiflowy.common.util.StringUtil;
import com.agentsflex.core.llm.Llm;
import com.agentsflex.llm.gitee.GiteeAiLlm;
import com.agentsflex.llm.gitee.GiteeAiLlmConfig;
import com.agentsflex.llm.ollama.OllamaLlm;
import com.agentsflex.llm.ollama.OllamaLlmConfig;
import com.agentsflex.llm.openai.OpenAILlm;
import com.agentsflex.llm.openai.OpenAILlmConfig;
import com.agentsflex.llm.qwen.QwenLlm;
import com.agentsflex.llm.qwen.QwenLlmConfig;
import com.agentsflex.llm.spark.SparkLlm;
import com.agentsflex.llm.spark.SparkLlmConfig;
import com.mybatisflex.annotation.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.util.StringUtils;
import java.util.Map;

/**
 * 实体类。
 *
 * @author michael
 * @since 2024-08-23
 */

@Table("tb_ai_llm")
public class AiLlm extends AiLlmBase {

    public List<String> getSupportFeatures() {
        List<String> features = new ArrayList<>();
        if (getSupportChat() != null && getSupportChat()) {
            features.add("对话");
        }

        if (getSupportFunctionCalling() != null && getSupportFunctionCalling()) {
            features.add("方法调用");
        }

        if (getSupportEmbed() != null && getSupportEmbed()) {
            features.add("Embedding");
        }

        if (getSupportReranker() != null && getSupportReranker()) {
            features.add("重排");
        }

        if (getSupportTextToImage() != null && getSupportTextToImage()) {
            features.add("文生图");
        }

        if (getSupportImageToImage() != null && getSupportImageToImage()) {
            features.add("图生图");
        }

        if (getSupportTextToAudio() != null && getSupportTextToAudio()) {
            features.add("文生音频");
        }

        if (getSupportAudioToAudio() != null && getSupportAudioToAudio()) {
            features.add("音频转音频");
        }

        if (getSupportTextToVideo() != null && getSupportTextToVideo()) {
            features.add("文生视频");
        }

        if (getSupportImageToVideo() != null && getSupportImageToVideo()) {
            features.add("图生视频");
        }

        if (getOptions() != null && !getOptions().isEmpty()) {
            Boolean multimodal = (Boolean) getOptions().get("multimodal");
            if (multimodal != null && multimodal) {
                features.add("多模态");
            }
        }

        return features;
    }

    public Llm toLlm() {
        String brand = getBrand();
        if (StringUtil.noText(brand)) {
            return null;
        }
        switch (brand.toLowerCase()) {
            case "spark":
                return sparkLlm();
            case "ollama":
                return ollamaLlm();
            default:
                return openaiLLm();
        }
    }

    private Llm giteeLlm() {
        GiteeAiLlmConfig giteeAiLlmConfig = new GiteeAiLlmConfig();
        giteeAiLlmConfig.setEndpoint(getLlmEndpoint());
        giteeAiLlmConfig.setApiKey(getLlmApiKey());
        giteeAiLlmConfig.setModel(getLlmModel());
//        giteeAiLlmConfig.setDebug(true);
        return new GiteeAiLlm(giteeAiLlmConfig);
    }

    private Llm qwenLlm() {
        QwenLlmConfig qwenLlmConfig = new QwenLlmConfig();
        qwenLlmConfig.setEndpoint(getLlmEndpoint());
        qwenLlmConfig.setApiKey(getLlmApiKey());
        qwenLlmConfig.setModel(getLlmModel());
//        qwenLlmConfig.setDebug(true);
        return new QwenLlm(qwenLlmConfig);
    }

    private Llm ollamaLlm() {
        OllamaLlmConfig ollamaLlmConfig = new OllamaLlmConfig();
        ollamaLlmConfig.setEndpoint(getLlmEndpoint());
        ollamaLlmConfig.setApiKey(getLlmApiKey());
        ollamaLlmConfig.setModel(getLlmModel());
//        ollamaLlmConfig.setDebug(true);
        return new OllamaLlm(ollamaLlmConfig);
    }

    private Llm openaiLLm() {
        OpenAILlmConfig openAiLlmConfig = new OpenAILlmConfig();
        openAiLlmConfig.setEndpoint(getLlmEndpoint());
        openAiLlmConfig.setApiKey(getLlmApiKey());
        openAiLlmConfig.setModel(getLlmModel());
        openAiLlmConfig.setDefaultEmbeddingModel(getLlmModel());
        openAiLlmConfig.setDebug(true);
        Map<String,Object> extraConfigMap = getLlmExtraConfig();
        String chatPath = (String)extraConfigMap.get("chatPath");
        String embedPath = (String)extraConfigMap.get("embedPath");
        if (StringUtils.hasLength(chatPath)){
            openAiLlmConfig.setChatPath(chatPath);
        }

        if (StringUtils.hasLength(embedPath)){
            openAiLlmConfig.setEmbedPath(embedPath);
        }
        // if (llmExtraConfig != null && !llmExtraConfig.isEmpty()) {
        //     Properties prop = PropertiesUtil.textToProperties(llmExtraConfig);
        //     String chatPath = prop.getProperty("chatPath");
        //     String embedPath = prop.getProperty("embedPath");
        //     System.out.println("chatPath" + chatPath);
        //     System.out.println("embdaPath" + embedPath);
        //     if (chatPath != null && !chatPath.isEmpty()) {
        //         openAiLlmConfig.setChatPath(chatPath);
        //     }
        //     if (embedPath != null && !embedPath.isEmpty()) {
        //         openAiLlmConfig.setEmbedPath(embedPath);
        //     }
        // }
        return new OpenAILlm(openAiLlmConfig);
    }

    private Llm sparkLlm() {

        SparkLlmConfig sparkLlmConfig = new SparkLlmConfig();

        Map<String,Object> extraConfigMap = getLlmExtraConfig();
        String version = (String)extraConfigMap.get("version");
        String appId = (String)extraConfigMap.get("appId");
        String apiSecret = (String)extraConfigMap.get("apiSecret");
        sparkLlmConfig.setApiSecret(apiSecret);
        sparkLlmConfig.setVersion(version);
        sparkLlmConfig.setAppId(appId);
        sparkLlmConfig.setApiKey(getLlmApiKey());

        // SparkLlmConfig sparkLlmConfig = PropertiesUtil.propertiesTextToEntity(getLlmExtraConfig(),
        //     SparkLlmConfig.class);
        
//        sparkLlmConfig.setDebug(true);
        return new SparkLlm(sparkLlmConfig);
    }

    private Llm deepseekLlm() {
        DeepseekConfig config = new DeepseekConfig();
        config.setModel(getLlmModel());
        config.setEndpoint(getLlmEndpoint());
        config.setApiKey(getLlmApiKey());
//        config.setDebug(true);
        return new DeepseekLlm(config);
    }
}

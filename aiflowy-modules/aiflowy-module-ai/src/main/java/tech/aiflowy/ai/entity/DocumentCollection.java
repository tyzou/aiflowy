package tech.aiflowy.ai.entity;

import com.agentsflex.core.model.chat.tool.Tool;
import com.agentsflex.core.store.DocumentStore;
import com.agentsflex.store.aliyun.AliyunVectorStore;
import com.agentsflex.store.aliyun.AliyunVectorStoreConfig;
import com.agentsflex.store.elasticsearch.ElasticSearchVectorStore;
import com.agentsflex.store.elasticsearch.ElasticSearchVectorStoreConfig;
import com.agentsflex.store.opensearch.OpenSearchVectorStore;
import com.agentsflex.store.opensearch.OpenSearchVectorStoreConfig;
import com.agentsflex.store.qcloud.QCloudVectorStore;
import com.agentsflex.store.qcloud.QCloudVectorStoreConfig;
import com.agentsflex.store.redis.RedisVectorStore;
import com.agentsflex.store.redis.RedisVectorStoreConfig;
import com.mybatisflex.annotation.Table;
import tech.aiflowy.ai.agentsflex.tool.DocumentCollectionTool;
import tech.aiflowy.ai.entity.base.DocumentCollectionBase;
import tech.aiflowy.common.util.PropertiesUtil;
import tech.aiflowy.common.web.exceptions.BusinessException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 实体类。
 *
 * @author michael
 * @since 2024-08-23
 */

@Table("tb_document_collection")
public class DocumentCollection extends DocumentCollectionBase {

    /**
     * 文档块问题集合配置key
     */
    public static final String KEY_CHUNK_QUESTION_VECTOR_STORE_COLLECTION = "chunkQuestionVectorStoreCollection";

    /**
     * 文档块摘要集合配置key
     */
    public static final String KEY_CHUNK_SUMMARY_VECTOR_STORE_COLLECTION = "chunkSummaryVectorStoreCollection";

    /**
     * 知识召回最大条数
     */
    public static final String KEY_DOC_RECALL_MAX_NUM = "docRecallMaxNum";

    /**
     * 相似度最小值
     */
    public static final String KEY_SIMILARITY_THRESHOLD = "simThreshold";

    /**
     * 搜索引擎类型
     */
    public static final String KEY_SEARCH_ENGINE_TYPE = "searchEngineType";

    /**
     * 是否允许更新向量模型
     */
    public static final String KEY_CAN_UPDATE_EMBEDDING_MODEL = "canUpdateEmbeddingModel";

    public DocumentStore toDocumentStore() {
        String storeType = this.getVectorStoreType();
        if (storeType == null) {
            return null;
        }
        switch (storeType.toLowerCase()) {
            case "redis":
                return redisStore();
//            case "milvus":
//                return milvusStore();
            case "opensearch":
                return openSearchStore();
            case "elasticsearch":
                return elasticSearchStore();
            case "aliyun":
                return aliyunStore();
            case "qcloud":
                return qcloudStore();
        }
        return null;
    }

    public boolean isVectorStoreEnabled() {
        return this.getVectorStoreEnable() != null && this.getVectorStoreEnable();
    }

    public boolean isSearchEngineEnabled() {
        return this.getSearchEngineEnable() != null && this.getSearchEngineEnable();
    }


    private DocumentStore redisStore() {
        RedisVectorStoreConfig redisVectorStoreConfig = getStoreConfig(RedisVectorStoreConfig.class);
        return new RedisVectorStore(redisVectorStoreConfig);
    }

//    private DocumentStore milvusStore() {
//        MilvusVectorStoreConfig milvusVectorStoreConfig = getStoreConfig(MilvusVectorStoreConfig.class);
//        return new MilvusVectorStore(milvusVectorStoreConfig);
//    }

    private DocumentStore openSearchStore() {
        OpenSearchVectorStoreConfig openSearchVectorStoreConfig = getStoreConfig(OpenSearchVectorStoreConfig.class);
        return new OpenSearchVectorStore(openSearchVectorStoreConfig);
    }

    private DocumentStore elasticSearchStore() {
        ElasticSearchVectorStoreConfig elasticSearchVectorStoreConfig = getStoreConfig(ElasticSearchVectorStoreConfig.class);
        return new ElasticSearchVectorStore(elasticSearchVectorStoreConfig);
    }

    private DocumentStore aliyunStore() {
        AliyunVectorStoreConfig aliyunVectorStoreConfig = getStoreConfig(AliyunVectorStoreConfig.class);
        return new AliyunVectorStore(aliyunVectorStoreConfig);
    }

    private DocumentStore qcloudStore() {
        QCloudVectorStoreConfig qCloudVectorStoreConfig = getStoreConfig(QCloudVectorStoreConfig.class);
        return new QCloudVectorStore(qCloudVectorStoreConfig);
    }

    private <T> T getStoreConfig(Class<T> clazz) {
        return PropertiesUtil.propertiesTextToEntity(this.getVectorStoreConfig(), clazz);
    }

    public Tool toFunction(boolean needEnglishName) {
        return new DocumentCollectionTool(this, needEnglishName);
    }

    public Object getOptionsByKey(String key) {
        Map<String, Object> options = this.getOptions();
        if (options == null) {
            return null;
        }
        if (KEY_DOC_RECALL_MAX_NUM.equals(key) && !options.containsKey(KEY_DOC_RECALL_MAX_NUM)) {
            return 5;
        }
        if (KEY_SIMILARITY_THRESHOLD.equals(key)) {
            if (!options.containsKey(KEY_SIMILARITY_THRESHOLD)) {
                return 0.6f;
            } else {
                BigDecimal score = (BigDecimal) options.get(key);
                return (float) score.doubleValue();
            }
        }
        if (KEY_SEARCH_ENGINE_TYPE.equals(key)) {
            if (!options.containsKey(KEY_SEARCH_ENGINE_TYPE)) {
                return "lucene";
            }
        }
        return options.get(key);
    }
}

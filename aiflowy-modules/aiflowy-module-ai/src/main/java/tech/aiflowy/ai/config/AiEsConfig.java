package tech.aiflowy.ai.config;

import com.agentsflex.engine.es.ESConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiEsConfig extends ESConfig {

    @Value("${rag.searcher.elastic.host}")
    @Override
    public void setHost(String host) {
        super.setHost(host);
    }

    @Value("${rag.searcher.elastic.userName}")
    @Override
    public void setUserName(String userName) {
        super.setUserName(userName);
    }

    @Value("${rag.searcher.elastic.password}")
    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Value("${rag.searcher.elastic.indexName}")
    @Override
    public void setIndexName(String indexName) {
        super.setIndexName(indexName);
    }

}


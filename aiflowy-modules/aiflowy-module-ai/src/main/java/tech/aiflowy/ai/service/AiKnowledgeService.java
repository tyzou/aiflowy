package tech.aiflowy.ai.service;

import com.agentsflex.core.document.Document;
import tech.aiflowy.ai.entity.AiKnowledge;
import tech.aiflowy.common.domain.Result;
import com.mybatisflex.core.service.IService;

import java.math.BigInteger;
import java.util.List;

/**
 *  服务层。
 *
 * @author michael
 * @since 2024-08-23
 */
public interface AiKnowledgeService extends IService<AiKnowledge> {

    List<Document> search(BigInteger id, String keyword);

    AiKnowledge getDetail(String idOrAlias);

    AiKnowledge getByAlias(String idOrAlias);
}

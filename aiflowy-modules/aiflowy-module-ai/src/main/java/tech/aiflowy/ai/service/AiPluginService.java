package tech.aiflowy.ai.service;

import com.mybatisflex.core.service.IService;
import tech.aiflowy.ai.entity.AiPlugin;
import tech.aiflowy.common.domain.Result;

import java.util.List;

/**
 *  服务层。
 *
 * @author WangGangqiang
 * @since 2025-04-25
 */
public interface AiPluginService extends IService<AiPlugin> {

    boolean savePlugin(AiPlugin aiPlugin);

    boolean removePlugin(String id);

    boolean updatePlugin(AiPlugin aiPlugin);

    List<AiPlugin> getList();

    Result pageByCategory(Long pageNumber, Long pageSize, int category);
}

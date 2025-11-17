package tech.aiflowy.ai.service;

import com.mybatisflex.core.service.IService;
import tech.aiflowy.ai.entity.AiPluginCategories;
import tech.aiflowy.ai.entity.AiPluginCategoryRelation;
import tech.aiflowy.common.domain.Result;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *  服务层。
 *
 * @author Administrator
 * @since 2025-05-21
 */
public interface AiPluginCategoryRelationService extends IService<AiPluginCategoryRelation> {

    boolean updateRelation(long pluginId, ArrayList<Integer> categoryIds);

    List<AiPluginCategories> getPluginCategories(long pluginId);
}

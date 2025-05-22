package tech.aiflowy.ai.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import tech.aiflowy.ai.entity.AiPlugin;
import tech.aiflowy.ai.entity.AiPluginCategoryRelation;
import tech.aiflowy.ai.mapper.AiPluginCategoryRelationMapper;
import tech.aiflowy.ai.mapper.AiPluginMapper;
import tech.aiflowy.ai.service.AiPluginService;
import org.springframework.stereotype.Service;
import tech.aiflowy.common.domain.Result;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *  服务层实现。
 *
 * @author WangGangqiang
 * @since 2025-04-25
 */
@Service
public class AiPluginServiceImpl extends ServiceImpl<AiPluginMapper, AiPlugin>  implements AiPluginService {

    @Resource
    AiPluginMapper aiPluginMapper;

    @Resource
    AiPluginCategoryRelationMapper aiPluginCategoryRelationMapper;

    @Override
    public Result savePlugin(AiPlugin aiPlugin) {
        System.out.println("aaa");
        aiPlugin.setCreated(new Date());
        int insert = aiPluginMapper.insert(aiPlugin);
        if (insert <= 0){
            return Result.fail(1, "保存失败");
        }
        return Result.success();
    }

    @Override
    public Result removePlugin(String id) {
        int remove =  aiPluginMapper.deleteById(id);
        if (remove <= 0){
            return Result.fail(1, "删除失败");

        }
        return Result.success();

    }

    @Override
    public Result updatePlugin(AiPlugin aiPlugin) {
        QueryWrapper queryWrapper = QueryWrapper.create().select("id")
                .from("tb_ai_plugin")
                .where("id = ?", aiPlugin.getId());
        int update = aiPluginMapper.updateByQuery(aiPlugin, queryWrapper);
        if (update <= 0){
            return Result.fail(1, "修改失败");

        }
        return Result.success();
    }

    @Override
    public Result getList() {
        QueryWrapper queryWrapper = QueryWrapper.create().select("id, name, description, icon")
                .from("tb_ai_plugin");
        List<AiPlugin> aiPlugins = aiPluginMapper.selectListByQueryAs(queryWrapper, AiPlugin.class);
        return Result.success(aiPlugins);
    }

    @Override
    public Result pageByCategory(Long pageNumber, Long pageSize, int category) {
        // 通过分类查询插件
        QueryWrapper queryWrapper = QueryWrapper.create().select("plugin_id")
                .from("tb_ai_plugin_category_relation")
                .where("category_id = ? ", category);
        // 分页查询该分类中的插件
        Page<BigInteger> pagePluginIds = aiPluginCategoryRelationMapper.paginateAs(new Page<>(pageNumber, pageSize), queryWrapper, BigInteger.class);
        Page<AiPluginCategoryRelation> paginateCategories =  aiPluginCategoryRelationMapper.paginate(pageNumber, pageSize, queryWrapper);
        List<AiPlugin> aiPlugins = Collections.emptyList();
        if (paginateCategories.getRecords().isEmpty()){
            return Result.success(new Page<>(aiPlugins, pageNumber, pageSize, paginateCategories.getTotalRow()));
        }
        List<BigInteger> pluginIds = pagePluginIds.getRecords();
        // 查询对应的插件信息
        QueryWrapper queryPluginWrapper = QueryWrapper.create().select("*")
                .from("tb_ai_plugin")
                .in("id", pluginIds);
        aiPlugins = aiPluginMapper.selectListByQuery(queryPluginWrapper);
        Page<AiPlugin> aiPluginPage = new Page<>(aiPlugins, pageNumber, pageSize, paginateCategories.getTotalRow());
        return Result.success(aiPluginPage);
    }


}

package tech.aiflowy.common.ai.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PluginParamConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将JSON字符串转换为PluginParam对象列表
     * @param jsonStr 数据库中的JSON字符串
     * @return PluginParam对象列表
     */
    public static List<PluginParam> convertFromJson(String jsonStr) {
        try {
            // 将JSON字符串解析为List<Map>结构
            List<Map<String, Object>> paramMaps = objectMapper.readValue(
                    jsonStr,
                    new TypeReference<List<Map<String, Object>>>(){}
            );

            List<PluginParam> result = new ArrayList<>();
            for (Map<String, Object> paramMap : paramMaps) {
                result.add(convertMapToPluginParam(paramMap));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to PluginParam", e);
        }
    }

    /**
     * 递归将Map转换为PluginParam对象
     */
    private static PluginParam convertMapToPluginParam(Map<String, Object> map) {
        PluginParam param = new PluginParam();
        param.setKey(getStringValue(map, "key"));
        param.setName(getStringValue(map, "name"));
        param.setDescription(getStringValue(map, "description"));
        param.setType(getStringValue(map, "type"));
        param.setMethod(getStringValue(map, "method"));
        param.setDefaultValue(map.get("defaultValue"));
        param.setRequired(getBooleanValue(map, "required"));
        param.setEnabled(getBooleanValue(map, "enabled"));

        // 处理子节点
        if (map.containsKey("children")) {
            Object childrenObj = map.get("children");
            if (childrenObj instanceof List) {
                List<Map<String, Object>> childrenMaps = (List<Map<String, Object>>) childrenObj;
                List<PluginParam> children = new ArrayList<>();
                for (Map<String, Object> childMap : childrenMaps) {
                    children.add(convertMapToPluginParam(childMap));
                }
                param.setChildren(children);
            }
        }

        return param;
    }

    private static String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private static boolean getBooleanValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }
}
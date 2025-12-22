package tech.aiflowy.common.ai.plugin;
import java.util.*;

public class NestedParamConverter {

    public static Map<String, Object> convertToNestedParamMap(List<PluginParam> pluginParams) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (pluginParams == null || pluginParams.isEmpty()) return result;

        for (PluginParam param : pluginParams) {
            if (!param.isEnabled()) continue;
            result.put(param.getName(), buildValue(param));
        }

        return result;
    }

    private static Object buildValue(PluginParam param) {
        if ("String".equalsIgnoreCase(param.getType())) {
            return param.getDefaultValue();
        } else if ("Object".equalsIgnoreCase(param.getType())) {
            Map<String, Object> objMap = new LinkedHashMap<>();
            if (param.getChildren() != null && !param.getChildren().isEmpty()) {
                for (PluginParam child : param.getChildren()) {
                    objMap.put(child.getName(), buildValue(child));
                }
            }
            return objMap;
        } else if ("Array".equalsIgnoreCase(param.getType())) {
            if (param.getChildren() != null && !param.getChildren().isEmpty()) {
                PluginParam arrayItemTemplate = param.getChildren().get(0);
                if ("Array[Object]".equalsIgnoreCase(arrayItemTemplate.getType())) {
                    List<Map<String, Object>> list = new ArrayList<>();

                    Map<String, Object> item = new LinkedHashMap<>();
                    for (PluginParam child : arrayItemTemplate.getChildren()) {
                        item.put(child.getName(), buildValue(child));
                    }

                    list.add(item); // 示例中只添加一个对象
                    return list;
                }
            }
            return Collections.EMPTY_LIST;
        }

        return null;
    }
}

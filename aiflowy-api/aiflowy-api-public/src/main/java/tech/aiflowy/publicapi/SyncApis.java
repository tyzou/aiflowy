package tech.aiflowy.publicapi;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.mybatisflex.core.MybatisFlexBootstrap;
import com.mybatisflex.core.query.QueryWrapper;
import com.zaxxer.hikari.HikariDataSource;
import tech.aiflowy.system.entity.SysApiKeyResource;
import tech.aiflowy.system.mapper.SysApiKeyResourceMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 同步接口到数据库
 */
public class SyncApis {

    public static void main(String[] args) throws Exception {

        try (HikariDataSource dataSource = new HikariDataSource()) {
            dataSource.setJdbcUrl("jdbc:mysql://192.168.2.10:3306/aiflowy-v2?useInformationSchema=true&characterEncoding=utf-8");
            dataSource.setUsername("root");
            dataSource.setPassword("123456");

            MybatisFlexBootstrap bootstrap = MybatisFlexBootstrap.getInstance();
            bootstrap.setDataSource(dataSource);
            bootstrap.addMapper(SysApiKeyResourceMapper.class);
            bootstrap.start();

            SysApiKeyResourceMapper mapper = bootstrap.getMapper(SysApiKeyResourceMapper.class);
            String dir = System.getProperty("user.dir") + "/aiflowy-api/aiflowy-api-public/src/main/java/tech/aiflowy/publicapi/controller";
            List<String> filePath = getAllFilePaths(dir);
            for (String path : filePath) {
                extractCommentsFromFile(path, mapper);
            }
        }
    }

    public static List<String> getAllFilePaths(String directoryPath) throws IOException {
        Path startPath = Paths.get(directoryPath);

        try (Stream<Path> stream = Files.walk(startPath)) {
            return stream
                    .filter(Files::isRegularFile)      // 只获取文件，排除目录
                    .map(Path::toAbsolutePath)         // 转换为绝对路径
                    .map(Path::toString)               // 转换为字符串
                    .collect(Collectors.toList());
        }
    }

    public static void extractCommentsFromFile(String filePath, SysApiKeyResourceMapper mapper) throws Exception {
        System.out.println("正在解析文件: " + filePath);
        FileInputStream in = new FileInputStream(filePath);
        CompilationUnit cu = StaticJavaParser.parse(in);

        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
            // 1. 获取类级别的 RequestMapping 路径
            String classPath = "";
            Optional<AnnotationExpr> classMapping = c.getAnnotationByName("RequestMapping");
            if (classMapping.isPresent()) {
                classPath = getAnnotationValue(classMapping.get());
            }

            String finalClassPath = classPath; //用于lambda中使用
            String className = c.getNameAsString();
            String classComment = c.getJavadoc().map(d -> d.getDescription().toText()).orElse("");

            System.out.println("=========================================");
            System.out.println("类名: " + className);
            System.out.println("类注释: " + classComment);
            System.out.println("类路径: " + finalClassPath);

            // 2. 遍历方法
            c.getMethods().forEach(method -> {
                // 查找常见的 Mapping 注解
                String[] mappingTypes = {"GetMapping", "PostMapping", "PutMapping", "DeleteMapping", "PatchMapping", "RequestMapping"};

                for (String mappingType : mappingTypes) {
                    Optional<AnnotationExpr> methodMapping = method.getAnnotationByName(mappingType);
                    if (methodMapping.isPresent()) {
                        // 获取方法上的路径
                        String methodPath = getAnnotationValue(methodMapping.get());

                        // 拼接完整 URI
                        String fullUri = combinePaths(finalClassPath, methodPath);

                        // 获取请求方式 (如果是 RequestMapping，通常默认为 All 或者需要进一步解析 method 属性，这里简单处理)
                        String httpMethod = mappingType.replace("Mapping", "").toUpperCase();
                        if (httpMethod.equals("REQUEST")) httpMethod = "ALL";

                        // 获取方法注释
                        String methodComment = method.getJavadoc().map(doc -> doc.getDescription().toText()).orElse("");

                        System.out.println("--------------------------------");
                        System.out.println("  方法名: " + method.getNameAsString());
                        System.out.println("  类型: " + httpMethod);
                        System.out.println("  完整URI: " + fullUri);
                        System.out.println("  方法注释: " + methodComment);

                        // 可以在这里调用 mapper 存入数据库
                        QueryWrapper w = QueryWrapper.create();
                        w.eq(SysApiKeyResource::getRequestInterface, fullUri);
                        SysApiKeyResource record = mapper.selectOneByQuery(w);
                        if (record != null) {
                            record.setTitle(methodComment);
                            mapper.insertOrUpdate(record);
                        } else {
                            record = new SysApiKeyResource();
                            record.setRequestInterface(fullUri);
                            record.setTitle(methodComment);
                            mapper.insert(record);
                        }
                    }
                }
            });
        });
    }

    /**
     * 解析注解中的 value 或 path 值
     * 处理几种情况:
     * 1. @GetMapping("/api") -> SingleMemberAnnotationExpr
     * 2. @GetMapping(value = "/api") -> NormalAnnotationExpr
     * 3. @GetMapping(path = "/api") -> NormalAnnotationExpr
     * 4. @GetMapping -> 默认为空字符串
     */
    private static String getAnnotationValue(AnnotationExpr annotation) {
        // 情况 1: @GetMapping("/path")
        if (annotation instanceof SingleMemberAnnotationExpr) {
            String value = ((SingleMemberAnnotationExpr) annotation).getMemberValue().toString();
            return removeQuotes(value);
        }
        // 情况 2: @GetMapping(value="/path") 或 @GetMapping(path="/path")
        else if (annotation instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr normal = (NormalAnnotationExpr) annotation;
            for (MemberValuePair pair : normal.getPairs()) {
                String key = pair.getNameAsString();
                if ("value".equals(key) || "path".equals(key)) {
                    return removeQuotes(pair.getValue().toString());
                }
            }
        }
        // 情况 3: @GetMapping (没有参数)
        return "";
    }

    /**
     * 去除 JavaParser 解析出的字符串中的双引号
     */
    private static String removeQuotes(String value) {
        if (value == null) return "";
        return value.replace("\"", "").trim();
    }

    /**
     * 拼接类路径和方法路径，处理斜杠
     */
    private static String combinePaths(String classPath, String methodPath) {
        if (classPath == null) classPath = "";
        if (methodPath == null) methodPath = "";

        // 确保以 / 开头
        if (!classPath.startsWith("/") && !classPath.isEmpty()) classPath = "/" + classPath;
        if (!methodPath.startsWith("/") && !methodPath.isEmpty()) methodPath = "/" + methodPath;

        // 如果 classPath 只有 /，去掉它，避免 //method
        if (classPath.equals("/")) classPath = "";

        String full = classPath + methodPath;
        // 处理重复斜杠 (例如 class=/api/ method=/list -> /api//list)
        return full.replace("//", "/");
    }
}

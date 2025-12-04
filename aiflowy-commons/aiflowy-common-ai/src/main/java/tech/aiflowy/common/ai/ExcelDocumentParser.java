//package tech.aiflowy.common.ai;
//
//import cn.idev.excel.EasyExcel;
//import cn.idev.excel.read.listener.PageReadListener;
//import com.agentsflex.core.document.Document;
//import com.agentsflex.core.file2text.extractor.FileExtractor;
//import com.agentsflex.core.file2text.source.DocumentSource;
//import com.alibaba.fastjson.JSON;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class ExcelDocumentParser implements FileExtractor {
//    public Document parse(InputStream inputStream) {
//        List<List<String>> tableData = new ArrayList<>();
//        List<String> headerRow = new ArrayList<>();
//
//        // 使用 EasyExcel 读取 Excel 输入流
//        EasyExcel.read(inputStream, new PageReadListener<Map<Integer, String>>(dataList -> {
//                    for (Map<Integer, String> row : dataList) {
//                        List<String> rowData = new ArrayList<>();
//                        for (int i = 0; i < row.size(); i++) {  // 注意这里是 i < row.size()
//                            rowData.add(row.getOrDefault(i, ""));  // 防止 null 值
//                        }
//
//                        if (headerRow.isEmpty()) {
//                            // 第一行作为表头
//                            headerRow.addAll(rowData);
//                            tableData.add(headerRow);
//                        } else {
//                            // 添加数据行
//                            tableData.add(rowData);
//                        }
//                    }
//                }))
////                .headRowNumber(0)  // 关键：不要跳过任何行
//                .sheet()           // 默认第一个 sheet
//                .doRead();
////        String plainText = generateMarkdownTable(tableData);
//
//        // 创建并返回 Document 对象
//        return new Document(JSON.toJSONString(tableData));
//    }
//
//
//    @Override
//    public boolean supports(DocumentSource source) {
//        return false;
//    }
//
//    @Override
//    public String extractText(DocumentSource source) throws IOException {
//        return "";
//    }
//}

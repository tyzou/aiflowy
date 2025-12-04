package tech.aiflowy.common.ai;

import com.agentsflex.core.file2text.extractor.FileExtractor;
import com.agentsflex.core.file2text.extractor.impl.*;

public class DocumentParserFactory {

    public static FileExtractor getDocumentParser(String typeOrFileName) {
        if (typeOrFileName == null) {
            throw new NullPointerException("typeOrFileName can not be null");
        } else {
            typeOrFileName = typeOrFileName.trim().toLowerCase();
        }
        if (typeOrFileName.endsWith(".pdf")) {
            return new PdfTextExtractor();
        }
        if (typeOrFileName.endsWith(".docx")) {
            return new DocxExtractor();
        }
        if (typeOrFileName.endsWith(".doc")) {
            return new DocExtractor();
        }
        if (typeOrFileName.endsWith(".html") || typeOrFileName.endsWith(".htm")) {
            return new HtmlExtractor();
        }
        if (typeOrFileName.endsWith(".md") || typeOrFileName.endsWith(".txt") ||
                typeOrFileName.endsWith(".csv") || typeOrFileName.endsWith(".xml")
                || typeOrFileName.endsWith(".json") || typeOrFileName.endsWith(".log")
        ) {
            return new PlainTextExtractor();
        }
        if (typeOrFileName.endsWith(".ppt") || typeOrFileName.endsWith(".pptx")) {
            return new PptxExtractor();
        }
        return null;
    }
}

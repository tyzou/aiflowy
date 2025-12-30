package tech.aiflowy.common.constant.enums;

import tech.aiflowy.common.annotation.DictDef;

@DictDef(name = "反馈类型", code = "feedbackType", keyField = "code", labelField = "text")
public enum EnumFeedbackType {

    UNREAD(0,"未查看"),
    VIEWED(1,"已查看"),
    PROCESSED(2, "已处理");

    private final int code;
    private final String text;

    EnumFeedbackType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}

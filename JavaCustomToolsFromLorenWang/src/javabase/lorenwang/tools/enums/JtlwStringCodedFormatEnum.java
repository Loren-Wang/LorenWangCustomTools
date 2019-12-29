package javabase.lorenwang.tools.enums;

public enum JtlwStringCodedFormatEnum {
    UTF_8("UTF-8"),
    UNICODE("UNICODE"),
    GBK("GBK"),
    ;
    /**
     * 编码格式
     */
    private String codedFormat;

    JtlwStringCodedFormatEnum(String codedFormat) {
        this.codedFormat = codedFormat;
    }

    public String getCodedFormat() {
        return codedFormat;
    }
}

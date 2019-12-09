package javabase.lorenwang.tools.enums;


/**
 * 功能作用：文件类型枚举
 * 创建时间：2019-12-06 16:13
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public enum JtlwFileTypeEnum {
    OTHER(0, "-------"),
    // images
    JPG(1, "FFD8FF"),
    JPEG(2, "FFD8FF"),
    PNG(3, "89504E47"),
    GIF(4, "47494638"),
    TIF(5, "49492A00"),
    BMP(6, "424D"),
    //
    DWG(7, "41433130"), // CAD
    PSD(8, "38425053"),
    RTF(9, "7B5C727466"), // 日记本
    EML(11, "44656C69766572792D646174653A"), // 邮件
    GZ(12, "1F8B08"),

    /**
     * 文档
     */
    DOC(13, "D0CF11E0"),
    XLS(14, "D0CF11E0"),//EXCEL2003版本文件
    MDB(15, "5374616E64617264204A"),
    PS(16, "252150532D41646F6265"),
    PDF(17, "255044462D312E"),
    DOCX(18, "504B0304"),
    XLSX(19, "504B0304"),//EXCEL2007以上版本文件
    /**
     * Outlook (pst).
     */
    PST(20, "2142444E"),
    /**
     * Outlook Express.
     */
    DBX(21, "CFAD12FEC5FD746F"),
    /**
     * MS Word/Excel.
     * XLS_DOC:ppt,doc,xls
     * XLSX_DOCX:xlsx
     */
    XLS_DOC(22, "D0CF11E0"),
    XLSX_DOCX(23, "504B030414000600080000002100"),
    /**
     * Visio
     */
    VSD(24, "d0cf11e0a1b11ae10000"),
    /**
     * WPS文字wps、表格et、演示dps都是一样的
     */
    WPS(25, "d0cf11e0a1b11ae10000"),
    /**
     * WordPerfect.
     */
    WPD(26, "FF575043"),
    /**
     * Postscript.
     */
    EPS(27, "252150532D41646F6265"),
    /**
     * TXT:txt,docx
     */
    TXT(28, "0000000000000000000000000000"),

    /**
     * 开发文件
     */
    /**
     * JSP Archive.
     */
    JSP(29, "3C2540207061676520"),
    /**
     * JAVA Archive.
     */
    JAVA(30, "7061636B61676520"),
    /**
     * CLASS Archive.
     */
    CLASS(31, "CAFEBABE0000002E00"),
    /**
     * JAR Archive.
     */
    JAR(32, "504B03040A000000"),
    /**
     * MF Archive.
     */
    MF(33, "4D616E69666573742D56"),
    /**
     * EXE Archive.
     */
    EXE(34, "4D5A9000030000000400"),
    /**
     * CHM Archive.
     */
    CHM(35, "49545346030000006000"),
    XML(36, "3C3F786D6C"),
    HTML(37, "68746D6C3E"),
    CSS(38, "48544D4C207B0D0A0942"),
    JS(39, "696B2E71623D696B2E71"),

    /**
     * 音视频文件
     */
    /**
     * Wave.
     */
    WAV(40, "57415645"),
    /**
     * AVI.
     */
    AVI(41, "41564920"),
    /**
     * Real Audio.
     */
    RAM(42, "2E7261FD"),
    /**
     * Real Media.
     */
    RM(43, "2E524D46"),
    /**
     * MPEG (mpg).
     */
    MPG(44, "000001BA"),
    /**
     * Quicktime.
     */
    MOV(45, "6D6F6F76"),
    /**
     * Windows Media.
     */
    ASF(46, "3026B2758E66CF11"),
    /**
     * MIDI.
     */
    MID(47, "4D546864"),
    /**
     * MP4.
     */
    MP4(48, "00000020667479706d70"),
    /**
     * MP3.
     */
    MP3(49, "49443303000000002176"),
    /**
     * FLV.
     */
    FLV(50, "464C5601050000000900"),


    /**
     * TIFF.
     */
    TIFF(51, "49492A00"),
    /**
     * torrent
     */
    TORRENT(52, "6431303A637265617465"),
    /**
     * Quicken.
     */
    QDF(53, "AC9EBD8F"),
    /**
     * Windows Password.
     */
    PWL(54, "E3828596"),
    /**
     * ZIP Archive.
     */
    ZIP(55, "504B0304"),
    /**
     * RAR Archive.
     */
    RAR(56, "52617221");

    private int type;
    private String start;

    JtlwFileTypeEnum(int type, String start) {
        this.type = type;
        this.start = start;
    }

    public int getType() {
        return type;
    }

    public String getStart() {
        return start;
    }

//    /**
//     * 获取所有文档相关类型
//     */
//    fun getDocType(): Array<JtlwFileTypeEnum> {
//        return arrayOf(DOC, XLS, PDF, DOCX, XLSX, MDB, PST, DBX, XLSX_DOCX, XLS_DOC, VSD, WPS, WPD, EPS, TXT)
//    }
//
//    /**
//     * 所有文件类型
//     */
//    fun getImageType(): Array<JtlwFileTypeEnum> {
//        return arrayOf(JPG, JPEG, PNG, BMP, GIF, TIF)
//    }
}

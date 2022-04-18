package javabase.lorenwang.network;

public class JnlwHttpRes {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 协议名称,http、https等
     */
    private String protocol;
    /**
     * 协议主版本号
     */
    private int major;
    /**
     * 协议副版本号
     */
    private int minor;
    /**
     * 数据体
     */
    private String data;
    /**
     * 失败状态code
     */
    private Integer failStatuesCode = null;
    /**
     * 异常信息
     */
    private Exception exception;

    JnlwHttpRes(String protocol, int major, int minor, String data) {
        success = true;
        this.protocol = protocol;
        this.major = major;
        this.minor = minor;
        this.data = data;
    }

    JnlwHttpRes(String protocol, int major, int minor, int failStatuesCode) {
        success = false;
        this.protocol = protocol;
        this.major = major;
        this.minor = minor;
        this.failStatuesCode = failStatuesCode;
    }

    JnlwHttpRes(Exception exception) {
        success = false;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getData() {
        return data;
    }

    public Integer getFailStatuesCode() {
        return failStatuesCode;
    }

    public Exception getException() {
        return exception;
    }
}

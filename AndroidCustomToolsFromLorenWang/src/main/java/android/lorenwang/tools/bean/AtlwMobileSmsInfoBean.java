package android.lorenwang.tools.bean;

import android.provider.Telephony;

/**
 * 功能作用：手机短消息实体
 * 创建时间：2019-11-28 下午 15:22:35
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：     * {@link Telephony.TextBasedSmsColumns.TYPE},
 * * {@link Telephony.TextBasedSmsColumns.ADDRESS},
 * * {@link Telephony.TextBasedSmsColumns.DATE},
 * * {@link Telephony.TextBasedSmsColumns.DATE_SENT},
 * * {@link Telephony.TextBasedSmsColumns.READ},
 * * {@link Telephony.TextBasedSmsColumns.STATUS},
 * * {@link Telephony.TextBasedSmsColumns.SUBJECT},
 * * {@link Telephony.TextBasedSmsColumns.BODY},
 * * {@link Telephony.TextBasedSmsColumns.PERSON},
 */

public class AtlwMobileSmsInfoBean {
    /**
     * 短信类型
     */
    private Integer type;
    /**
     * 短信发送方地址
     */
    private String sendAddress;
    /**
     * 短信接收时间
     */
    private Long receiveDate;
    /**
     * 短信发送时间
     */
    private Long sendDate;
    /**
     * 短信阅读状态，是否已读
     */
    private Boolean readStatus;
    /**
     * 短信短信主题，可能为空
     */
    private String subject;
    /**
     * 短信内容
     */
    private String body;
    /**
     * 短信发送方id，如果存在的话
     * reference to item in {@code content://contacts/people}
     */
    private Long personId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public Long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Long receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}

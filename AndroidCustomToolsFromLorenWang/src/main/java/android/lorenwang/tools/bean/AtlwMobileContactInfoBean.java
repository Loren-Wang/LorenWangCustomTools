package android.lorenwang.tools.bean;

import java.util.Comparator;

/**
 * 功能作用：手机联系人信息实体
 * 创建时间：2019-11-28 下午 15:22:35
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwMobileContactInfoBean implements Comparator<AtlwMobileContactInfoBean> {
    /**
     * 得到联系人名称
     */
    private String contactName;
    /**
     * 得到手机号码
     */
    private String phoneNumber;
    /**
     * 音序
     */
    private Character phoneticSequence;
    /**
     * 全拼
     */
    private String completeSpelling;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Character getPhoneticSequence() {
        return phoneticSequence;
    }

    public void setPhoneticSequence(Character phoneticSequence) {
        this.phoneticSequence = phoneticSequence;
    }

    public String getCompleteSpelling() {
        return completeSpelling;
    }

    public void setCompleteSpelling(String completeSpelling) {
        this.completeSpelling = completeSpelling;
    }

    @Override
    public int compare(AtlwMobileContactInfoBean o1, AtlwMobileContactInfoBean o2) {
        return o1.completeSpelling.compareTo(o2.completeSpelling);
    }
}

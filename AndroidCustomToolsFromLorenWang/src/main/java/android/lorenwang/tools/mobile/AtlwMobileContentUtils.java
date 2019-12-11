package android.lorenwang.tools.mobile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.lorenwang.tools.AtlwSetting;
import android.lorenwang.tools.bean.AtlwMobileContactInfoBean;
import android.lorenwang.tools.bean.AtlwMobileSmsInfoBean;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javabase.lorenwang.tools.common.JtlwVariateDataParamUtils;

/**
 * 功能作用：获取手机各个内容的单例类
 * 创建时间：2019-11-28 下午 15:14:40
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwMobileContentUtils {
    private final String TAG = "AtlwMobileContentUtils";
    private static AtlwMobileContentUtils optionsUtils;

    private AtlwMobileContentUtils() {
    }


    public static AtlwMobileContentUtils getInstance() {
        if (optionsUtils == null) {
            synchronized (AtlwMobileContentUtils.class) {
                if (optionsUtils == null) {
                    optionsUtils = new AtlwMobileContentUtils();
                }
            }
        }
        return optionsUtils;
    }

    /******************************************手机通讯录相关***************************************/

    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    /**
     * 联系人名称
     **/
    private ArrayList<String> mContactsName = new ArrayList<String>();
    /**
     * 联系人音序
     **/
    private ArrayList<Character> mContactsYinxu = new ArrayList<Character>();
    /**
     * 联系人全拼
     **/
    private ArrayList<String> mContactsQuanPin = new ArrayList<String>();
    /**
     * 联系人头像
     **/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    /**
     * 获取系统本机通讯录列表
     */
    private void getSystemContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                } else {
                    phoneNumber = phoneNumber.replace("-", "").replace("+86", "").replace(" ", "");
                }
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
                if ("".equals(contactName)) {
                    //Sim卡中没有联系人头像
                    mContactsName.add("#");
                    mContactsYinxu.add('#');
                    mContactsQuanPin.add("#");
                    mContactsNumber.add(phoneNumber);
                } else if (contactName.substring(0, 1).getBytes().length > 1) {
                    //Sim卡中没有联系人头像
                    mContactsName.add(contactName);
                    mContactsYinxu.add(JtlwVariateDataParamUtils.getInstance().getFirstPinYin(contactName));
                    mContactsQuanPin.add(JtlwVariateDataParamUtils.getInstance().getPinYin(contactName));
                    mContactsNumber.add(phoneNumber);
                } else {
                    //Sim卡中没有联系人头像
                    mContactsName.add(contactName);
                    mContactsYinxu.add(contactName.substring(0, 1).toCharArray()[0]);
                    mContactsQuanPin.add(contactName);
                    mContactsNumber.add(phoneNumber);
                }
            }

            phoneCursor.close();
        }
    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private void getSIMContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, null, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)) {
                    continue;
                } else {
                    phoneNumber = phoneNumber.replace("-", "").replace("+86", "").replace(" ", "");
                }
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);
                if ("".equals(contactName)) {
                    //Sim卡中没有联系人头像
                    mContactsName.add("#");
                    mContactsYinxu.add('#');
                    mContactsQuanPin.add("#");
                    mContactsNumber.add(phoneNumber);
                } else if (contactName.substring(0, 1).getBytes().length > 1) {
                    //Sim卡中没有联系人头像
                    mContactsName.add(contactName);
                    mContactsYinxu.add(JtlwVariateDataParamUtils.getInstance().getFirstPinYin(contactName));
                    mContactsQuanPin.add(JtlwVariateDataParamUtils.getInstance().getPinYin(contactName));
                    mContactsNumber.add(phoneNumber);
                } else {
                    //Sim卡中没有联系人头像
                    mContactsName.add(contactName);
                    mContactsYinxu.add(contactName.substring(0, 1).toCharArray()[0]);
                    mContactsQuanPin.add(contactName);
                    mContactsNumber.add(phoneNumber);
                }
            }

            phoneCursor.close();
        }
    }

    /**
     * 获取所有的联系人信息，包括手机存储以及sim卡中的
     *
     * @return 联系人信息列表
     */
    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    public List<AtlwMobileContactInfoBean> getAllContacts(Context context) {
        getSystemContacts(context);
        getSIMContacts(context);
        for (int i = 0; i < mContactsName.size(); i++) {
            for (int j = 0; j < mContactsName.size(); j++) {
                if (mContactsName.get(i).equals(mContactsName.get(j)) && mContactsNumber.get(i).equals(mContactsNumber.get(j)) && i != j) {
                    mContactsName.remove(i);
                    mContactsYinxu.remove(i);
                    mContactsQuanPin.remove(i);
                    mContactsNumber.remove(i);
                    i--;
                    break;
                }
            }
        }
        List<AtlwMobileContactInfoBean> contactDtoList = new ArrayList<>();
        AtlwMobileContactInfoBean contactDto;
        for (int i = 0; i < mContactsName.size(); i++) {
            contactDto = new AtlwMobileContactInfoBean();
            contactDto.setContactName(mContactsName.get(i));
            contactDto.setPhoneNumber(mContactsNumber.get(i));
            if (mContactsYinxu.get(i).charValue() >= 'a' && mContactsYinxu.get(i).charValue() <= 'z') {//字母需要转成大写
                contactDto.setPhoneticSequence((char) (mContactsYinxu.get(i) - ('a' - 'A')));
            } else {
                contactDto.setPhoneticSequence(mContactsYinxu.get(i));
            }
            contactDto.setCompleteSpelling(mContactsQuanPin.get(i));
            contactDtoList.add(contactDto);
        }
        Collections.sort(contactDtoList, new AtlwMobileContactInfoBean());
        return contactDtoList;
    }

    /**************************************短信相关*************************************************/

    /**
     * 获取库短消息表字段
     * {@link Telephony.TextBasedSmsColumns.TYPE},
     * {@link Telephony.TextBasedSmsColumns.ADDRESS},
     * {@link Telephony.TextBasedSmsColumns.DATE},
     * {@link Telephony.TextBasedSmsColumns.DATE_SENT},
     * {@link Telephony.TextBasedSmsColumns.READ},
     * {@link Telephony.TextBasedSmsColumns.STATUS},
     * {@link Telephony.TextBasedSmsColumns.SUBJECT},
     * {@link Telephony.TextBasedSmsColumns.BODY},
     * {@link Telephony.TextBasedSmsColumns.PERSON},
     **/
    private static final String[] SMS_PROJECTION = new String[]{
            "type", "address", "date", "date_sent",
            "read", "status", "subject", "body", "person"
    };
    //以下变量为返回数据在查询语句中的存储返回的位置
    private static final int INDEX_SMS_TYPE = 0;
    private static final int INDEX_SMS_ADDRESS = 1;
    private static final int INDEX_SMS_DATE = 2;
    private static final int INDEX_SMS_DATE_SENT = 3;
    private static final int INDEX_SMS_READ = 4;
    private static final int INDEX_SMS_STATUS = 5;
    private static final int INDEX_SMS_SUBJECT = 6;
    private static final int INDEX_SMS_BODY = 7;
    private static final int INDEX_SMS_PERSON = 8;
    //以上变量为返回数据在查询语句中的存储返回的位置


    /**
     * 获取系统短消息
     *
     * @return 系统短信列表
     */
    public List<AtlwMobileSmsInfoBean> getSystemSms() {
        List<AtlwMobileSmsInfoBean> list = new ArrayList<>();
        ContentResolver resolver = AtlwSetting.nowApplication.getContentResolver();
        // 获取手机系统短信
        Cursor smsCursor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            smsCursor = resolver.query(Telephony.Sms.CONTENT_URI, SMS_PROJECTION, null, null, null);
        } else {
            smsCursor = resolver.query(Uri.parse("content://sms"), SMS_PROJECTION, null, null, null);
        }
        return getAtlwMobileSmsInfoBeans(list, smsCursor);
    }

    /**
     * 得到手机SIM卡短信
     *
     * @return 得到手机SIM卡短信列表
     */
    private List<AtlwMobileSmsInfoBean> getSIMSms() {
        List<AtlwMobileSmsInfoBean> list = new ArrayList<>();
        ContentResolver resolver = AtlwSetting.nowApplication.getContentResolver();
        // 获取Sims卡联系人
        Cursor phoneCursor = resolver.query(Uri.parse("content://icc/adn"), null, null, null,
                null);
        return getAtlwMobileSmsInfoBeans(list, phoneCursor);
    }

    /**
     * 获取短信信箱实例
     *
     * @param list        实例列表
     * @param phoneCursor 数据库数据光标
     * @return 转换后的实例列表
     */
    private List<AtlwMobileSmsInfoBean> getAtlwMobileSmsInfoBeans(List<AtlwMobileSmsInfoBean> list, Cursor phoneCursor) {
        if (phoneCursor != null) {
            AtlwMobileSmsInfoBean bean;
            while (phoneCursor.moveToNext()) {
                bean = new AtlwMobileSmsInfoBean();
                bean.setType(phoneCursor.getInt(INDEX_SMS_TYPE));
                bean.setSendAddress(phoneCursor.getString(INDEX_SMS_ADDRESS));
                bean.setReceiveDate(phoneCursor.getLong(INDEX_SMS_DATE));
                bean.setSendDate(phoneCursor.getLong(INDEX_SMS_DATE_SENT));
                bean.setReadStatus(phoneCursor.getInt(INDEX_SMS_READ) != 0);
                bean.setSubject(phoneCursor.getString(INDEX_SMS_SUBJECT));
                bean.setBody(phoneCursor.getString(INDEX_SMS_BODY));
                bean.setPersonId(phoneCursor.getLong(INDEX_SMS_PERSON));
                list.add(bean);
            }
            phoneCursor.close();
        }
        return list;
    }

    /**
     * 获取所有的短信，包括手机存储以及sim卡中的
     *
     * @return 短信列表
     */
    @RequiresPermission(Manifest.permission.READ_SMS)
    public List<AtlwMobileSmsInfoBean> getAllSms() {
        List<AtlwMobileSmsInfoBean> list = getSystemSms();
        list.addAll(getSIMSms());
        return list;
    }

}

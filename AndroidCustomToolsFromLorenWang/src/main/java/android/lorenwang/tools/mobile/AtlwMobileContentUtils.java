package android.lorenwang.tools.mobile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.lorenwang.tools.bean.AtlwMobileContactInfoBean;
import android.net.Uri;
import android.provider.ContactsContract;
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

}

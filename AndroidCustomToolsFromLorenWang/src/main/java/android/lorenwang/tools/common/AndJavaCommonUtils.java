package android.lorenwang.tools.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * 创建时间：2019-01-29 下午 16:15:18
 * 创建人：王亮（Loren wang）
 * 功能作用：和java的通用方法
 * 思路：
 * 方法：1、uuid产生器
 *      2、数组转集合
 *      3、判断变量是否为空
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AndJavaCommonUtils {
    private final String TAG = "CommonUtils";
    private static AndJavaCommonUtils baseUtils;
    private AndJavaCommonUtils(){}
    public static AndJavaCommonUtils getInstance(){
        if(baseUtils == null){
            baseUtils = new AndJavaCommonUtils();
        }
        return (AndJavaCommonUtils) baseUtils;
    }

    /**
     * uuid产生器
     * @param isRemoveSpecialChar 是否移除特殊字符，中划线
     */
    public String generateUuid(boolean isRemoveSpecialChar){
        String uuid = UUID.randomUUID().toString();
        if(isRemoveSpecialChar){
            uuid = uuid.replaceAll("-","");
        }
        return uuid ;
    }

    /**
     * 数组转集合
     * @param arrays
     * @param <T>
     * @return
     */
    public <T> List<T> paramesArrayToList(T[] arrays){
        List<T> list = new ArrayList<>();
        if(arrays != null) {
            list.addAll(Arrays.asList(arrays));
        }
        return list;
    }


    /**
     * 判断变量是否为空
     *
     * @param str String
     * @return boolean
     */
    public<T> boolean isEmpty(T str) {
        return (str == null || "".equals(str)) ? true : false;
    }
}

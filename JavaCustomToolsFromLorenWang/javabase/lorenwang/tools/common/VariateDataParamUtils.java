package javabase.lorenwang.tools.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javabase.lorenwang.tools.base.BaseUtils;

/**
 * 创建时间：2019-01-28 下午 20:02:56
 * 创建人：王亮（Loren wang）
 * 功能作用：变量数据格式化
 * 思路：
 * 方法：1、格式化double变量的小数部分为指定数量  paramsDoubleToNum
 *      2、除去末尾的0字符操作  clearEndZeroAndParamsForDouble
 *      3、去掉回车换行符  clearStringBlank
 *      4、数组转集合  paramesArrayToList
 *      5、将map的所有key值转成集合   paramsHashMapKeyToArrayList
 *      6、格式化长整形到指定位数   paramsLongToNum
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class VariateDataParamUtils extends BaseUtils {
    private final String TAG = "VariateDataParamUtils";

    private VariateDataParamUtils() {
    }

    public static VariateDataParamUtils getInstance() {
        if (baseUtils == null) {
            baseUtils = new VariateDataParamUtils();
        }
        return (VariateDataParamUtils) baseUtils;
    }

    /**
     * 格式化double变量的小数部分为指定数量
     * @param data double 变量数据
     * @param num 小数点位数
     * @return 返回格式化后字符串，小数点部分为指定数量
     */
    public String paramsDoubleToNum(Double data, int num) {
        StringBuffer decimalsPattern = new StringBuffer(".");
        for (int i = 0; i < num; i++) {
            decimalsPattern.append("0");
        }
        if (data == null || data == 0) {
            return "0" + decimalsPattern.toString();
        }
        DecimalFormat df = new DecimalFormat("###,###" + decimalsPattern.toString());
        if (num > 0 && num < 1) {
            return "0" + df.format(num);
        } else {
            return df.format(num);
        }
    }

    /**
     * 格式化长整形到指定位数
     * @param time
     * @param num
     * @return
     */
    public Long paramsLongToNum(Long time,Integer num){
        if(time != null && num != null){
            int length = String.valueOf(time).length();
            int compareTo = num.compareTo(length);
            if(compareTo == 0){
                return time;
            }else if(compareTo < 0){
                return Double.valueOf(time / Math.pow(10,(length - num))).longValue();
            }else {
                return Double.valueOf(time * Math.pow(10,(length - num))).longValue();
            }
        }
        return 0l;
    }

    /**
     * 除去末尾的0字符操作
     * @param doubleNum 传入所要格式化的值
     * @param maxDecimalNum 所保留的最大的非0的小数点后的位数
     * @return
     */
    public String clearEndZeroAndParamsForDouble(Double doubleNum, Integer maxDecimalNum){
        String str = paramsDoubleToNum(doubleNum,maxDecimalNum);//先进行格式化
        if(str.indexOf(".") > 0){
            str = str.replaceAll("0+?$", "");//去掉多余的0
            str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return str;
    }

    /**
     * 去掉回车换行符
     * @param str
     * @return
     */
    public String clearStringBlank(String str) {
        if(str != null && !"".equals(str)) {
            if (str != null && !"".equals(str)) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(str);
                String strNoBlank = m.replaceAll("");
                return strNoBlank;
            } else {
                return str;
            }
        }else {
            return str;
        }
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
     * 将map的所有key值转成集合
     * @param map
     * @param <T>
     * @return
     */
    public <K,T> List<K> paramsHashMapKeyToArrayList(Map<K,List<T>> map ){
        List<K> list = new ArrayList<>();
        if(map == null){
            return list;
        }
        Iterator<Map.Entry<K, List<T>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            list.add(iterator.next().getKey());
        }
        return list;
    }

    /******************************************私有方法部分*****************************************/


}

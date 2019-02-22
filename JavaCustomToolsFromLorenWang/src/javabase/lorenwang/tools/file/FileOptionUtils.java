package javabase.lorenwang.tools.file;

import java.io.File;

import javabase.lorenwang.tools.common.VariateDataParamUtils;

/**
 * 创建时间：2019-01-28 下午 20:19:47
 * 创建人：王亮（Loren wang）
 * 功能作用：文件操作工具类
 * 思路：
 * 方法：1、格式化文件大小  paramsFileSize
 *      2、获取绝对路径下最后一个文件夹名称  getLastDirctoryName
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class FileOptionUtils {
    private final String TAG = "FileOptionUtils";
    private static FileOptionUtils baseUtils;
    private FileOptionUtils(){}
    public static FileOptionUtils getInstance(){
        if(baseUtils == null){
            baseUtils = new FileOptionUtils();
        }
        return (FileOptionUtils) baseUtils;
    }

    /**
     * 格式化文件大小
     * @param fileSize
     * @return
     */
    public String paramsFileSize(Long fileSize){
        if(fileSize.compareTo(1024l) < 0){
            return (VariateDataParamUtils.getInstance().paramsDoubleToNum(fileSize.doubleValue(),2) + "B");
        }else  if(fileSize.compareTo((long) Math.pow(1024,2)) < 0){
            return (VariateDataParamUtils.getInstance().paramsDoubleToNum(fileSize * 1.0 / 1024,2) + "KB");
        }else  if(fileSize.compareTo((long) Math.pow(1024,3)) < 0){
            return (VariateDataParamUtils.getInstance().paramsDoubleToNum(fileSize * 1.0 / 1024 / 1024,2) + "MB");
        }else  if(fileSize.compareTo((long) Math.pow(1024,4)) < 0){
            return (VariateDataParamUtils.getInstance().paramsDoubleToNum(fileSize * 1.0 / 1024 / 1024 / 1024,2) + "GB");
        }else {
            return "0B";
        }
    }

    /**
     * 获取绝对路径下最后一个文件夹名称
     * @param absolutePath
     * @return
     */
    public String getLastDirctoryName(String absolutePath){
        if(absolutePath == null){
            return "";
        }
        //创建新的，防止由于使用同一个对象对于调用该方法的值产生影响
        String path = absolutePath.intern();
        //判断是不是文件，是文件的话获取父文件夹路径
        File file = new File(path);
        if(file.isFile()){
            path = file.getParentFile().getAbsolutePath();
        }

        if(path.contains("/")) {
            //循环移除末尾的“/”，防止一个路径下有多个“/”
            while (path.substring(path.lastIndexOf("/")).intern().equals("/")) {
                path = path.substring(0, path.length() - 1);
            }
            path = path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }

}

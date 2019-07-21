package android.lorenwang.tools.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.lorenwang.tools.base.AtlwCheckUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.util.Xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javabase.lorenwang.tools.file.JtlwFileOptionUtils;

/**
 * 创建时间：2019-01-29 下午 15:41:0
 * 创建人：王亮（Loren wang）
 * 功能作用：文件操作工具类
 * 思路：
 * 方法：1、读取图片文件并获取字节
 * 2、将InputStream写入File\
 * 3、从指定路径的文件中读取Bytes
 * 4、从File中读取Bytes
 * 5、从InputStream中读取Bytes
 * 6、复制单个文件
 * 7、将bitmap写入File
 * 8、获取文件大小，单位B
 * 9、删除文件夹以及目录下的文件
 * 10、根据正则获取指定目录下的所有文件列表(使用递归扫描方式)
 * 11、根据正则获取指定目录下的所有文件列表(使用队列扫描方式)
 * 12、创建文件夹
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwFileOptionUtils {
    private final String TAG = "FileOptionUtils";
    private final int BUFFER_SIZE = 1024; // 流转换的缓存大小
    private static AtlwFileOptionUtils atlwFileOptionUtils;
    /**
     * 线程安全的队列,用于文件操作
     */
    private ConcurrentLinkedQueue<File> fileOptionsLinkedQueue;

    /**
     * 私有构造方法
     */
    private AtlwFileOptionUtils() {
    }

    public static AtlwFileOptionUtils getInstance() {
        synchronized (AtlwFileOptionUtils.class) {
            if (atlwFileOptionUtils == null) {
                atlwFileOptionUtils = new AtlwFileOptionUtils();
            }
        }
        return atlwFileOptionUtils;
    }

    /******************************************读取部分*********************************************/

    /**
     * 读取图片文件并获取字节
     *
     * @param context
     * @param isCheckPermisstion 是否检查权限
     * @param isCheckFile        是否检查文件
     * @param filePath           文件地址
     * @return
     */
    public byte[] readImageFileGetBytes(Context context, Boolean isCheckPermisstion, Boolean isCheckFile, String filePath) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkFileOptionsPermisstion(context)) {
            return null;
        }
        return JtlwFileOptionUtils.getInstance().readImageFileGetBytes(isCheckFile, filePath);
    }

    /**
     * 从指定路径的文件中读取Bytes
     */
    public byte[] readBytes(Context context, Boolean isCheckPermisstion, String path) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, path)) {
            return new byte[]{};
        }
        return JtlwFileOptionUtils.getInstance().readBytes(path);
    }

    /**
     * 从File中读取Bytes
     */
    public byte[] readBytes(Context context, Boolean isCheckPermisstion, File file) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file)) {
            return new byte[]{};
        }
        return JtlwFileOptionUtils.getInstance().readBytes(file);
    }

    /**
     * 从InputStream中读取Bytes
     */
    public byte[] readBytes(Context context, Boolean isCheckPermisstion, InputStream inputStream) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, inputStream)) {
            return new byte[]{};
        }
        return JtlwFileOptionUtils.getInstance().readBytes(inputStream);
    }


    /******************************************写入部分*********************************************/

    /**
     * 将InputStream写入File
     */
    public Boolean writeToFile(Context context, Boolean isCheckPermisstion, File file, InputStream inputStream, Boolean append) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file, inputStream)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().writeToFile(file, inputStream, append);
    }

    /**
     * 将文本写入文件
     */
    public Boolean writeToFile(Context context, Boolean isCheckPermisstion, File file, String text) {
        return writeToFile(context, isCheckPermisstion, file, text, Xml.Encoding.UTF_8.toString(), false);
    }

    /**
     * 将文本写入文件，同时决定是否为追加写入
     */
    public Boolean writeToFile(Context context, Boolean isCheckPermisstion, File file, String text, String encoding, Boolean append) {
        try {
            return writeToFile(context, isCheckPermisstion, file, new ByteArrayInputStream(text.getBytes(encoding)), append);
        } catch (UnsupportedEncodingException e) {
            AtlwLogUtils.logE(e);
            return false;
        }

    }

    /**
     * 将bitmap写入File
     */
    public Boolean writeToFile(Context context, Boolean isCheckPermisstion, File file, Bitmap bitmap, Bitmap.CompressFormat format) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file, bitmap)) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(format, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            AtlwLogUtils.logE(e);
            return false;
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                AtlwLogUtils.logE(e);
            }

        }
    }

    public boolean writeToFile(Context context, boolean isCheckPermisstion, File file, byte[] buffer) {
        return writeToFile(context, isCheckPermisstion, file, buffer, false);
    }

    public boolean writeToFile(Context context, boolean isCheckPermisstion, File file, byte[] buffer, boolean append) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file, buffer, append)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().writeToFile(file, buffer, append);
    }


    /******************************************其他文件操作部分**************************************/

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public Boolean copyFile(Context context, Boolean isCheckPermisstion, String oldPath, String newPath) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().copyFile(oldPath, newPath);
    }

    /**
     * 删除文件
     */
    public Boolean deleteFile(Context context, Boolean isCheckPermisstion, String url) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().deleteFile(url);
    }

    /**
     * 获取文件大小，单位B
     *
     * @param file
     * @param filtrationDir
     * @return
     */
    public Long getFileSize(Context context, Boolean isCheckPermisstion, File file, String filtrationDir) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return 0l;
        }
        return JtlwFileOptionUtils.getInstance().getFileSize(file, filtrationDir);
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public Boolean deleteDirectory(Context context, Boolean isCheckPermisstion, String filePath) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().deleteDirectory(filePath);
    }

    /**
     * 创建文件夹
     *
     * @param isCheckPermisstion 是否检测权限
     * @param path               路径
     * @param isParentDir  是否创建的是父级文件夹
     * @return
     */
    public boolean createDirectory(Context context, Boolean isCheckPermisstion, String path, boolean isParentDir) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().createDirectory(path, isParentDir);
    }

    /**
     * 根据正则获取指定目录下的所有文件列表(使用递归扫描方式)
     *
     * @param scanPath     要扫描的问题件路径
     * @param matchRegular 文件正则
     * @return 文件列表
     */
    public List<File> getFileListForMatchRecursionScan(Context context, Boolean isCheckPermisstion, String scanPath, String matchRegular) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return new ArrayList<>();
        }
        return JtlwFileOptionUtils.getInstance().getFileListForMatchRecursionScan(scanPath, matchRegular);
    }

    /**
     * 根据正则获取指定目录下的所有文件列表(使用队列扫描方式)
     *
     * @param context      上下文,用来做权限检测
     * @param scanPath     要扫描的文件路径
     * @param matchRegular 要返回的文件的正则格式
     * @return 扫描到的文件列表
     */
    public synchronized List<File> getFileListForMatchLinkedQueueScan(Context context, Boolean isCheckPermisstion, String scanPath, final String matchRegular) {
        if (isCheckPermisstion && !AtlwCheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return new ArrayList<>();
        }
        return JtlwFileOptionUtils.getInstance().getFileListForMatchLinkedQueueScan(scanPath, matchRegular);
    }
}

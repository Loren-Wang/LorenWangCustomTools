package android.lorenwang.tools.file;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwCheckUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.file.JtlwFileOptionUtils;

/**
 * 功能作用：文件操作工具类
 * 初始注释时间： 2021/1/21 3:44 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 读取图片文件并获取字节--readImageFileGetBytes(isCheckPermisstion, isCheckFile, filePath)
 * 读取文件并获取字节--readBytes(isCheckPermisstion, path/file/inputStream)
 * 写入文件--writeToFile(isCheckPermisstion, file，inputStream/text/bitmap/buffer,...)
 * 通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件--writeToFile(intent, saveFile)
 * 复制单个文件--copyFile(isCheckPermisstion, oldPath, newPath)
 * 删除文件--deleteFile(isCheckPermisstion, url)
 * 获取文件大小，单位B--getFileSize(isCheckPermisstion, file, filtrationDir)
 * 删除文件夹以及目录下的文件--deleteDirectory(isCheckPermisstion, filePath)
 * 创建文件夹--createDirectory(isCheckPermisstion, path, nowPathIsFile)
 * 根据正则获取指定目录下的所有文件列表(使用递归扫描方式)--getFileListForMatchRecursionScan(isCheckPermisstion, scanPath, matchRegular)
 * 根据正则获取指定目录下的所有文件列表(使用队列扫描方式)--getFileListForMatchLinkedQueueScan(isCheckPermisstion, scanPath, matchRegular)
 * 获取根目录文件夹地址--getBaseStorageDirPath()
 * 获取App系统文件夹地址--getAppSystemStorageDirPath(applicationId)
 * 根据uri获取图片文件地址--getUriPath(uri, dbKey)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwFileOptionUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwFileOptionUtil optionsInstance;
    /**
     * 流转换的缓存大小
     */
    private final int BUFFER_SIZE = 1024;
    /**
     * 线程安全的队列,用于文件操作
     */
    private ConcurrentLinkedQueue<File> fileOptionsLinkedQueue;

    private AtlwFileOptionUtil() {
    }

    public static AtlwFileOptionUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwFileOptionUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwFileOptionUtil();
                }
            }
        }
        return optionsInstance;
    }

    /*------------------------------------读取部分------------------------------------*/

    /**
     * 读取图片文件并获取字节
     *
     * @param isCheckPermisstion 是否检查权限
     * @param isCheckFile        是否检查文件
     * @param filePath           文件地址
     * @return 读取到的字节
     */
    public byte[] readImageFileGetBytes(Boolean isCheckPermisstion, Boolean isCheckFile, String filePath) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(AtlwConfig.nowApplication)) {
            return null;
        }
        return JtlwFileOptionUtils.getInstance().readImageFileGetBytes(isCheckFile, filePath);
    }

    /**
     * 从指定路径的文件中读取Bytes
     *
     * @param isCheckPermisstion 是否检测权限
     * @param path               文件地址
     * @return 读取到的字节
     */
    public byte[] readBytes(Boolean isCheckPermisstion, String path) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(path)) {
            return new byte[]{};
        }
        return JtlwFileOptionUtils.getInstance().readBytes(path);
    }

    /**
     * 从File中读取Bytes
     *
     * @param isCheckPermisstion 是否检测权限
     * @param file               文件
     * @return 读取到的字节
     */
    public byte[] readBytes(Boolean isCheckPermisstion, File file) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(file)) {
            return new byte[]{};
        }
        return JtlwFileOptionUtils.getInstance().readBytes(file);
    }

    /**
     * 从InputStream中读取Bytes
     *
     * @param isCheckPermisstion 是否检测权限
     * @param inputStream        输入六级
     * @return 读取到的字节
     */
    public byte[] readBytes(Boolean isCheckPermisstion, InputStream inputStream) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(inputStream)) {
            return new byte[]{};
        }
        return JtlwFileOptionUtils.getInstance().readBytes(inputStream);
    }


    /*------------------------------------写入部分------------------------------------*/

    /**
     * 将InputStream写入File
     *
     * @param isCheckPermisstion 是否检测权限
     * @param file               文件
     * @param inputStream        输入流
     * @param append             是否拼接
     * @return 是否成功
     */
    public Boolean writeToFile(Boolean isCheckPermisstion, File file, InputStream inputStream, Boolean append) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(file, inputStream)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().writeToFile(file, inputStream, append);
    }

    /**
     * 将文本写入文件
     *
     * @param isCheckPermisstion 是否检测权限
     * @param text               文本
     * @param file               文件
     * @return 是否成功
     */
    public Boolean writeToFile(Boolean isCheckPermisstion, File file, String text) {
        return writeToFile(isCheckPermisstion, file, text, Xml.Encoding.UTF_8.toString(), false);
    }

    /**
     * 将文本写入文件，同时决定是否为追加写入
     *
     * @param isCheckPermisstion 是否检测权限
     * @param file               文件
     * @param text               文本内容
     * @param encoding           文本编码
     * @param append             是否后续新增插入，不覆盖插入
     * @return 是否成功
     */
    public Boolean writeToFile(Boolean isCheckPermisstion, File file, String text, String encoding, Boolean append) {
        try {
            return writeToFile(isCheckPermisstion, file, new ByteArrayInputStream(text.getBytes(encoding)), append);
        } catch (UnsupportedEncodingException e) {
            AtlwLogUtil.logUtils.logE(e);
            return false;
        }

    }

    /**
     * @param isCheckPermisstion 是否检测权限
     * @param file               文件
     * @param bitmap             图片位图
     * @param format             图片格式
     *                           将bitmap写入File
     * @return 返回处理结果
     */
    public Boolean writeToFile(Boolean isCheckPermisstion, File file, Bitmap bitmap, Bitmap.CompressFormat format) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(file, bitmap)) {
            return false;
        }
        createDirectory(isCheckPermisstion, file.getAbsolutePath(), true);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(format, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logE(e);
            return false;
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (Exception e) {
                AtlwLogUtil.logUtils.logE(e);
            }

        }
    }

    public boolean writeToFile(boolean isCheckPermisstion, File file, byte[] buffer) {
        return writeToFile(isCheckPermisstion, file, buffer, false);
    }

    public boolean writeToFile(boolean isCheckPermisstion, File file, byte[] buffer, boolean append) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects(file, buffer, append)) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().writeToFile(file, buffer, append);
    }


    /**
     * 通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件
     *
     * @param data     intent
     * @param saveFile 保存地址
     * @return 返回新图片地址
     */
    public String writeToFile(@NotNull Intent data, @NotNull String saveFile) {
        if (saveFile.isEmpty() || AtlwConfig.nowApplication == null || AtlwConfig.nowApplication.getContentResolver() == null) {
            return null;
        }
        if (data.getData() != null) {
            //目标文件夹
            InputStream inputStream = null;//文件图片输入流
            try {
                inputStream = AtlwConfig.nowApplication.getContentResolver().openInputStream(data.getData());
                boolean state = writeToFile(true, new File(saveFile), inputStream, false);
                if (state) {
                    return saveFile;
                } else {
                    return null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return null;
        }
    }


    /*------------------------------------其他文件操作部分------------------------------------*/

    /**
     * 复制单个文件
     *
     * @param isCheckPermisstion 是否检测权限
     * @param oldPath            String 原文件路径 如：c:/fqf.txt
     * @param newPath            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public Boolean copyFile(Boolean isCheckPermisstion, String oldPath, String newPath) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().copyFile(oldPath, newPath);
    }

    /**
     * 删除文件
     * @param isCheckPermisstion 是否检测权限
     * @param url                要删除的地址
     *                           删除文件
     * @return 返回删除结果
     */
    public Boolean deleteFile(Boolean isCheckPermisstion, String url) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().deleteFile(url);
    }

    /**
     * 获取文件大小，单位B
     *
     * @param isCheckPermisstion 是否检测权限
     * @param file               文件地址
     * @param filtrationDir      过滤的地址
     * @return 文件大小
     */
    public Long getFileSize(Boolean isCheckPermisstion, File file, String filtrationDir) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return 0L;
        }
        return JtlwFileOptionUtils.getInstance().getFileSize(file, filtrationDir);
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param isCheckPermisstion 是否检测权限
     * @param filePath           被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public Boolean deleteDirectory(Boolean isCheckPermisstion, String filePath) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().deleteDirectory(filePath);
    }

    /**
     * 创建文件夹
     *
     * @param isCheckPermisstion 是否检测权限
     * @param path               路径
     * @param nowPathIsFile      当前路径是否是文件
     * @return 文件夹创建结果
     */
    public boolean createDirectory(Boolean isCheckPermisstion, String path, boolean nowPathIsFile) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return false;
        }
        return JtlwFileOptionUtils.getInstance().createDirectory(path, nowPathIsFile);
    }

    /**
     * 根据正则获取指定目录下的所有文件列表(使用递归扫描方式)
     *
     * @param isCheckPermisstion 是否检测权限
     * @param scanPath           要扫描的问题件路径
     * @param matchRegular       文件正则
     * @return 文件列表
     */
    public List<File> getFileListForMatchRecursionScan(Boolean isCheckPermisstion, String scanPath, String matchRegular) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return new ArrayList<>();
        }
        return JtlwFileOptionUtils.getInstance().getFileListForMatchRecursionScan(scanPath, matchRegular);
    }

    /**
     * 根据正则获取指定目录下的所有文件列表(使用队列扫描方式)
     *
     * @param isCheckPermisstion 是否检测权限
     * @param scanPath           要扫描的文件路径
     * @param matchRegular       要返回的文件的正则格式
     * @return 扫描到的文件列表
     */
    public synchronized List<File> getFileListForMatchLinkedQueueScan(Boolean isCheckPermisstion, String scanPath, final String matchRegular) {
        if (isCheckPermisstion && !AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
            return new ArrayList<>();
        }
        return JtlwFileOptionUtils.getInstance().getFileListForMatchLinkedQueueScan(scanPath, matchRegular);
    }

    /**
     * 获取根目录文件夹地址
     *
     * @return 根目录文件夹地址
     */
    public String getBaseStorageDirPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/";
    }

    /**
     * 获取App系统文件夹地址
     *
     * @param applicationId App的包名
     * @return 根目录文件夹地址
     */
    public String getAppSystemStorageDirPath(String applicationId) {
        return getBaseStorageDirPath() + "Android/Data/" + applicationId + "/";
    }

    /**
     * 根据uri获取图片文件地址
     *
     * @param uri   uri地址
     * @param dbKey 数据库地址代表的字段
     * @return 地址
     */
    public String getUriPath(Uri uri, String dbKey) {
        String path = "";
        if (uri != null && !JtlwCheckVariateUtils.getInstance().isEmpty(dbKey)) {
            final String scheme = uri.getScheme();
            if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
                path = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = AtlwConfig.nowApplication.getContentResolver().query(uri, new String[]{dbKey}, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(dbKey);
                        if (index > -1) {
                            path = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            }
        }
        return path;
    }
}

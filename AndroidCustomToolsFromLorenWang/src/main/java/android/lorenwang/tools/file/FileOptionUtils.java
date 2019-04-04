package android.lorenwang.tools.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.lorenwang.tools.base.CheckUtils;
import android.lorenwang.tools.base.LogUtils;
import android.util.Xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

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
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class FileOptionUtils {
    private final String TAG = "FileOptionUtils";
    private final int BUFFER_SIZE = 1024; // 流转换的缓存大小
    private static FileOptionUtils fileOptionUtils;

    public static FileOptionUtils getInstance() {
        if (fileOptionUtils == null) {
            fileOptionUtils = new FileOptionUtils();
        }
        return fileOptionUtils;
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
        if (isCheckPermisstion && !CheckUtils.getInstance().checkFileOptionsPermisstion(context)) {
            return null;
        }
        if (isCheckFile && CheckUtils.getInstance().checkFileIsExit(filePath)
                && CheckUtils.getInstance().checkFileIsImage(filePath)) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            return bytes;
        } catch (Exception e) {
            LogUtils.logE(TAG, "图片读取异常");
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从指定路径的文件中读取Bytes
     */
    public byte[] readBytes(Context context, Boolean isCheckPermisstion, String path) {
        if (isCheckPermisstion && !CheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, path)) {
            return new byte[]{};
        }
        try {
            File file = new File(path);
            return readBytes(context, isCheckPermisstion, file);
        } catch (Exception e) {
            LogUtils.logE(e);
            return new byte[]{};
        }

    }

    /**
     * 从File中读取Bytes
     */
    public byte[] readBytes(Context context, Boolean isCheckPermisstion, File file) {
        if (isCheckPermisstion && !CheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file)) {
            return new byte[]{};
        }
        FileInputStream fis = null;
        try {
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
            return readBytes(context, isCheckPermisstion, fis);
        } catch (Exception e) {
            LogUtils.logE(e);
            return new byte[]{};
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                LogUtils.logE(e);
            }

        }
    }

    /**
     * 从InputStream中读取Bytes
     */
    public byte[] readBytes(Context context, Boolean isCheckPermisstion, InputStream inputStream) {
        if (isCheckPermisstion && !CheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, inputStream)) {
            return new byte[]{};
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = inputStream.read(buffer);
            while (length != -1) {
                baos.write(buffer, 0, length);
                baos.flush();
                length = inputStream.read(buffer);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            LogUtils.logE(e);
            return new byte[]{};
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                LogUtils.logE(e);
            }

        }
    }


    /******************************************写入部分*********************************************/

    /**
     * 将InputStream写入File
     */
    public Boolean writeToFile(Context context, Boolean isCheckPermisstion, File file, InputStream inputStream, Boolean append) {
        if (isCheckPermisstion && !CheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file, inputStream)) {
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, append);
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = inputStream.read(buffer);
            while (length != -1) {
                fos.write(buffer, 0, length);
                fos.flush();
                length = inputStream.read(buffer);
            }
            return true;
        } catch (Exception e) {
            LogUtils.logE(e);
            return false;
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                LogUtils.logE(e);
            }
        }
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
            LogUtils.logE(e);
            return false;
        }

    }

    /**
     * 将bitmap写入File
     */
    public Boolean writeToFile(Context context, Boolean isCheckPermisstion, File file, Bitmap bitmap, Bitmap.CompressFormat format) {
        if (isCheckPermisstion && !CheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context, file, bitmap)) {
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
            LogUtils.logE(e);
            return false;
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                LogUtils.logE(e);
            }

        }
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
        if (isCheckPermisstion && !CheckUtils.getInstance().checkIOUtilsOptionsPermissionAndObjects(context)) {
            return false;
        }
        FileOutputStream fs = null;
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                FileInputStream inStream = new FileInputStream(oldPath); //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length = inStream.read(buffer);
                while (length != -1) {
                    fs.write(buffer, 0, length);
                    fs.flush();
                    length = inStream.read(buffer);
                }
                inStream.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 删除文件
     */
    public Boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 获取文件大小，单位B
     *
     * @param file
     * @param filtrationDir
     * @return
     */
    public Long getFileSize(File file, String filtrationDir) {
        long fileSize = 0L;
        if (file.isFile() && file.exists()) {
            fileSize += file.length();
        } else if (file.exists()) {
            for (File file1 : file.listFiles()) {
                if (file1.isFile()) {
                    fileSize += file1.length();
                } else if (file.isDirectory() || filtrationDir == null || filtrationDir != file.getAbsolutePath()) {
                    fileSize += getFileSize(file1, filtrationDir);
                }
            }
        }
        return fileSize;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public Boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) {
            return false;
        } else {
            return dirFile.delete();
        }
        //删除当前空目录
    }
}

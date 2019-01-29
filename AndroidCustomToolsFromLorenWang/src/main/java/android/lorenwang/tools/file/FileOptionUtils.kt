package android.lorenwang.tools.file

import android.content.Context
import android.graphics.Bitmap
import android.lorenwang.tools.base.BaseUtils
import android.lorenwang.tools.base.CheckUtils
import android.lorenwang.tools.base.LogUtils
import java.io.*

/**
 * 创建时间：2019-01-29 下午 15:41:0
 * 创建人：王亮（Loren wang）
 * 功能作用：文件操作工具类
 * 思路：
 * 方法：1、读取图片文件并获取字节
 *      2、将InputStream写入File\
 *      3、从指定路径的文件中读取Bytes
 *      4、从File中读取Bytes
 *      5、从InputStream中读取Bytes
 *      6、复制单个文件
 *      7、将bitmap写入File
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class FileOptionUtils : BaseUtils() {
    private val TAG = "FileOptionUtils"
    private val BUFFER_SIZE = 1024; // 流转换的缓存大小

    companion object {
        val instance: FileOptionUtils
            get() {
                if (BaseUtils.baseUtils == null) {
                    BaseUtils.baseUtils = FileOptionUtils()
                }
                return BaseUtils.baseUtils as FileOptionUtils
            }
    }

    /******************************************读取部分*********************************************/

    /**
     * 读取图片文件并获取字节
     * @param context
     * @param isCheckPermisstion 是否检查权限
     * @param isCheckFile 是否检查文件
     * @param filePath 文件地址
     * @return
     */
    fun readImageFileGetBytes(context: Context, isCheckPermisstion: Boolean, isCheckFile: Boolean, filePath: String): ByteArray? {
        if (isCheckPermisstion && !CheckUtils.getInstance().checkFileOptionsPermisstion(context)) {
            return null
        }
        if (isCheckFile && CheckUtils.getInstance().checkFileIsExit(filePath)
                && CheckUtils.getInstance().checkFileIsImage(filePath)) {
            return null
        }
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(filePath)
            val bytes = ByteArray(fileInputStream.available())
            fileInputStream.read(bytes)
            return bytes
        } catch (e: Exception) {
            LogUtils.logE(TAG,"图片读取异常")
            return null
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 从指定路径的文件中读取Bytes
     */
    fun readBytes(context: Context, isCheckPermisstion: Boolean, path: String): ByteArray? {
        if (isCheckPermisstion && !CheckUtils.checkIOUtilsOptionsPermissionAndObjects(context, path)) {
            return byteArrayOf()
        }
        try {
            val file = File(path)
            return readBytes(context, isCheckPermisstion, file)
        } catch (e: Exception) {
            LogUtils.logE(e)
            return null
        }

    }

    /**
     * 从File中读取Bytes
     */
    fun readBytes(context: Context, isCheckPermisstion: Boolean, file: File): ByteArray? {
        if (isCheckPermisstion && !CheckUtils.checkIOUtilsOptionsPermissionAndObjects(context, file)) {
            return byteArrayOf()
        }
        var fis: FileInputStream? = null
        try {
            if (!file.exists()) {
                return null
            }
            fis = FileInputStream(file)
            return readBytes(context, isCheckPermisstion, fis)
        } catch (e: Exception) {
            LogUtils.logE(e)
            return null
        } finally {
            try {
                fis?.close()
            } catch (e: Exception) {
                LogUtils.logE(e)
            }

        }
    }

    /**
     * 从InputStream中读取Bytes
     */
    fun readBytes(context: Context, isCheckPermisstion: Boolean, inputStream: InputStream): ByteArray? {
        if (isCheckPermisstion && !CheckUtils.checkIOUtilsOptionsPermissionAndObjects(context, inputStream)) {
            return byteArrayOf()
        }
        var baos: ByteArrayOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            val buffer = ByteArray(BUFFER_SIZE)
            var length = inputStream.read(buffer, 0, BUFFER_SIZE)
            while (length != 0) {
                baos.write(buffer, 0, length)
                baos.flush()
                length = inputStream.read(buffer, 0, BUFFER_SIZE)
            }
            return baos.toByteArray()
        } catch (e: Exception) {
            LogUtils.logE(e)
            return null
        } finally {
            try {
                baos?.close()
            } catch (e: Exception) {
                LogUtils.logE(e)
            }

        }
    }


    /******************************************写入部分*********************************************/

    /**
     * 将InputStream写入File
     */
    fun writeToFile(context: Context, isCheckPermisstion: Boolean, file: File, inputStream: InputStream): Boolean {
        if (isCheckPermisstion && !CheckUtils.checkIOUtilsOptionsPermissionAndObjects(context, file, inputStream)) {
            return false
        }

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            val buffer = ByteArray(BUFFER_SIZE)
            var length = inputStream.read(buffer, 0, BUFFER_SIZE)
            while (length != 0) {
                fos.write(buffer, 0, length)
                fos.flush()
                length = inputStream.read(buffer, 0, BUFFER_SIZE)
            }
            return true
        } catch (e: Exception) {
            LogUtils.logE(e)
            return false
        } finally {
            try {
                fos?.close()
            } catch (e: Exception) {
                LogUtils.logE(e)
            }
        }
    }

    /**
     * 将bitmap写入File
     */
    fun writeToFile(context: Context, isCheckPermisstion: Boolean, file: File, bitmap: Bitmap, format: Bitmap.CompressFormat): Boolean {
        if (isCheckPermisstion && !CheckUtils.checkIOUtilsOptionsPermissionAndObjects(context, file, bitmap)) {
            return false
        }

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(format, 100, fos)
            fos.flush()
            fos.close()
            return true
        } catch (e: Exception) {
            LogUtils.logE(e)
            return false
        } finally {
            try {
                fos?.close()
            } catch (e: Exception) {
                LogUtils.logE(e)
            }

        }
    }


    /******************************************其他文件操作部分**************************************/

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    fun copyFile(context: Context, isCheckPermisstion: Boolean, oldPath: String, newPath: String): Boolean {
        if (isCheckPermisstion && !CheckUtils.checkIOUtilsOptionsPermissionAndObjects(context)) {
            return false
        }
        var fs: FileOutputStream? = null
        try {
            val oldfile = File(oldPath)
            if (oldfile.exists()) { //文件存在时
                val inStream = FileInputStream(oldPath) //读入原文件
                fs = FileOutputStream(newPath)
                val buffer = ByteArray(1444)
                var length = inStream.read(buffer, 0, BUFFER_SIZE)
                while (length != 0) {
                    fs.write(buffer, 0, length)
                    fs.flush()
                    length = inStream.read(buffer, 0, BUFFER_SIZE)
                }
                inStream.close()
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            if (fs != null) {
                try {
                    fs.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }

    /**
     * 删除文件
     */
    fun deleteFile(url: String): Boolean {
        var result = false
        val file = File(url)
        if (file.exists()) {
            result = file.delete()
        }
        return result
    }
}

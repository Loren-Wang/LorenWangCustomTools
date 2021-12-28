package javabase.lorenwang.tools.file;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import javabase.lorenwang.tools.JtlwLogUtil;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;
import javabase.lorenwang.tools.common.JtlwCommonUtil;
import javabase.lorenwang.tools.common.JtlwDateTimeUtil;
import javabase.lorenwang.tools.common.JtlwVariateDataParamUtil;
import javabase.lorenwang.tools.enums.JtlwFileTypeEnum;

/**
 * 功能作用：文件操作工具类
 * 创建时间：2019-01-28 下午 20:19:47
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 读取图片文件并获取字节--readImageFileGetBytes(isCheckFile,filePath)
 * 从指定路径的文件中读取Bytes--readBytes(path)
 * 从File中读取Bytes--readBytes(file)
 * 从InputStream中读取Bytes--readBytes(inputStream)
 * 将InputStream写入File--writeToFile(file,inputStream,append)
 * 将文本写入文件--writeToFile(file,text)
 * 将文本写入文件，同时决定是否为追加写入--writeToFile(file,text,encoding,append)
 * 将byte数组写入文件--writeToFile(file,buffer)
 * 将byte数组写入文件，是否追加--writeToFile(file,buffer,append)
 * 格式化文件大小--paramsFileSize(fileSize)
 * 复制单个文件--copyFile(oldPath,newPath)
 * 文件夹复制--copyFileDir(oldPath,newPath)
 * 压缩文件夹--compressToZip(sourcePath,outPutPath)
 * 删除文件--deleteFile(path)
 * 获取文件大小，单位B--getFileSize(file,filtrationDir)
 * 删除文件夹以及目录下的文件--deleteDirectory(filePath)
 * 获取绝对路径下最后一个文件夹名称--getLastDirectoryName(absolutePath)
 * 创建文件夹--createDirectory(path,nowPathIsFile)
 * 根据正则获取指定目录下的所有文件列表(使用递归扫描方式)--getFileListForMatchRecursionScan(scanPath,matchRegular)
 * 根据正则获取指定目录下的所有文件列表(使用队列扫描方式)--getFileListForMatchLinkedQueueScan(scanPath,matchRegular)
 * 清理指定文件夹下所有的空文件夹--clearEmptyFileDir(dirPath)
 * 获取文件类型--getFileType(filePath)
 * 获取文件类型--getFileType(inputStream)
 * 获取文件编码格式--getFileCodedFormat(filePath)
 * 修改文件编码格式--changeFileCodedFormat(filePath,oldCodedFormat,newCodedFormat)
 * 以指定编码方式读取文件，返回文件内容--readFileContent(filePath,codedFormat)
 * 以指定编码方式写文本文件，存在会覆盖--writeFilContent(filePath,toCharsetName,content)
 * 获取所有文档相关类型--getDocType()
 * 获取所有图片的相关类型--getImageType()
 * 重命名文件--renameFile(oldFile,newFileName)
 * 添加文字水印-addTextWaterMark(srcPath,outPath,locationX,locationY,text,textColor)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：Android10以上使用最好在application加上： android:requestLegacyExternalStorage="true"，否则会导致listFiles()大小为0
 */
public class JtlwFileOptionUtil {
    private final String TAG = "FileOptionUtils";
    private static volatile JtlwFileOptionUtil baseUtils;
    /**
     * 线程安全的队列,用于文件操作
     */
    private ConcurrentLinkedQueue<File> fileOptionsLinkedQueue;
    /**
     * 流转换的缓存大小
     */
    private final int BUFFER_SIZE = 1024;

    public JtlwFileOptionUtil() {
        allDocFileTypeEnum = new ArrayList<>();
    }

    public static JtlwFileOptionUtil getInstance() {
        synchronized (JtlwFileOptionUtil.class) {
            if (baseUtils == null) {
                baseUtils = new JtlwFileOptionUtil();
            }
        }
        return baseUtils;
    }

    /*----------------------------------------读取部分--------------------------------------------*/

    /**
     * 读取图片文件并获取字节
     *
     * @param isCheckFile 是否检查文件
     * @param filePath    文件地址
     * @return 文件字节
     */
    public byte[] readImageFileGetBytes(Boolean isCheckFile, String filePath) {
        if (isCheckFile && !JtlwCheckVariateUtil.getInstance().checkFileIsExit(filePath) && !JtlwCheckVariateUtil.getInstance().checkFileIsImage(
                filePath)) {
            return null;
        }
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[2048];
            int length;
            while ((length = fileInputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            JtlwLogUtil.logUtils.logE(TAG, "图片读取异常");
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从指定路径的文件中读取Bytes
     *
     * @param path 要读物的文件路径
     * @return 读取后的文件字节数组，不会为空
     */
    public byte[] readBytes(String path) {
        try {
            File file = new File(path);
            return readBytes(file);
        } catch (Exception e) {
            JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            return new byte[]{};
        }
    }

    /**
     * 从File中读取Bytes
     *
     * @param file 要读物的文件
     * @return 读取后的文件字节数组，不会为空
     */
    public byte[] readBytes(File file) {
        FileInputStream fis = null;
        try {
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
            return readBytes(fis);
        } catch (Exception e) {
            JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            return new byte[]{};
        } finally {
            try {
                assert fis != null;
                fis.close();
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            }

        }
    }

    /**
     * 从InputStream中读取Bytes
     *
     * @param inputStream 文件读取数据流
     * @return 读取后的文件字节数组，不会为空
     */
    public byte[] readBytes(InputStream inputStream) {
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
            JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            return new byte[]{};
        } finally {
            try {
                assert baos != null;
                baos.close();
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            }

        }
    }


    /*----------------------------------------写入部分----------------------------------------****/

    /**
     * 将InputStream写入File
     *
     * @param file        要写入的文件
     * @param inputStream 写入文件流
     * @param append      是否将数据内容拼接到最后
     * @return 读取后的文件字节数组，不会为空
     */
    public Boolean writeToFile(File file, InputStream inputStream, boolean append) {
        //删除文件
        if (!append) {
            deleteFile(file.getAbsolutePath());
        }
        //创建父级文件夹
        createDirectory(file.getAbsolutePath(), true);
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
            JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            return false;
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            }
        }
    }

    /**
     * 将文本写入文件
     *
     * @param file 要写入的目标文件
     * @param text 要写入的内容文本
     * @return 返回写入是否成功，成功为true
     */
    public boolean writeToFile(File file, String text) {
        return writeToFile(file, text, "UTF-8", false);
    }

    /**
     * 将文本写入文件，同时决定是否为追加写入
     *
     * @param file     要写入的目标文件
     * @param text     要写入的内容文本
     * @param encoding 写入内容编码
     * @param append   是否追加写入
     * @return 返回写入是否成功，成功为true
     */
    public Boolean writeToFile(File file, String text, String encoding, boolean append) {
        try {
            return writeToFile(file, new ByteArrayInputStream(text.getBytes(encoding)), append);
        } catch (UnsupportedEncodingException e) {
            JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            return false;
        }

    }

    /**
     * 将byte数组写入文件
     *
     * @param file   文件
     * @param buffer 要存入的数组
     * @return 写入结果
     */
    public boolean writeToFile(File file, byte[] buffer) {
        return writeToFile(file, buffer, false);
    }

    /**
     * 将byte数组写入文件，是否追加
     *
     * @param file   文件
     * @param buffer 要存入的数组
     * @param append 是否追加
     * @return 写入结果
     */
    public boolean writeToFile(File file, byte[] buffer, boolean append) {
        FileOutputStream fos = null;
        try {
            //删除文件
            deleteFile(file.getAbsolutePath());
            //创建父级文件夹
            createDirectory(file.getAbsolutePath(), true);
            fos = new FileOutputStream(file, append);
            fos.write(buffer);
            return true;
        } catch (Exception e) {
            JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, JtlwCheckVariateUtil.getInstance().isEmpty(e) ? "" : e.getMessage());
            }
        }
    }

    /*----------------------------------------其他文件操作部分--------------------------------------*/

    /**
     * 格式化文件大小
     *
     * @param fileSize 文件大小
     * @return 文件大小
     */
    public String paramsFileSize(Long fileSize) {
        if (fileSize.compareTo(1024L) < 0) {
            return (JtlwVariateDataParamUtil.getInstance().paramsDoubleToNum(fileSize.doubleValue(), 2) + "B");
        } else if (fileSize.compareTo((long) Math.pow(1024, 2)) < 0) {
            return (JtlwVariateDataParamUtil.getInstance().paramsDoubleToNum(fileSize * 1.0 / 1024, 2) + "KB");
        } else if (fileSize.compareTo((long) Math.pow(1024, 3)) < 0) {
            return (JtlwVariateDataParamUtil.getInstance().paramsDoubleToNum(fileSize * 1.0 / 1024 / 1024, 2) + "MB");
        } else if (fileSize.compareTo((long) Math.pow(1024, 4)) < 0) {
            return (JtlwVariateDataParamUtil.getInstance().paramsDoubleToNum(fileSize * 1.0 / 1024 / 1024 / 1024, 2) + "GB");
        } else {
            return "0B";
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public Boolean copyFile(String oldPath, String newPath) {
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
     * 文件夹复制
     *
     * @param oldPath 旧文件夹,如果是文件的话则直接变成文件复制
     * @param newPath 新文件夹
     * @return 复制结果，有一个失败就是失败
     */
    public boolean copyFileDir(String oldPath, String newPath) {
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(oldPath) && JtlwCheckVariateUtil.getInstance().isNotEmpty(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);
            //文件类型判断处理
            if (!oldFile.exists() || newFile.exists()) {
                //旧文件夹不存在或者新文件夹存在
                return false;
            }
            //新文件夹判断处理
            createDirectory(newPath, oldFile.isFile());
            if (oldFile.isFile()) {
                //是文件，直接复制
                return copyFile(oldPath, newPath);
            } else {
                boolean status = true;
                //是文件夹开始处理
                for (File file : oldFile.listFiles()) {
                    if (file.isFile()) {
                        status = status && copyFile(file.getAbsolutePath(), newFile.getAbsolutePath() + "/" + file.getName());
                    } else {
                        //是文件夹，递归处理
                        status = status && copyFileDir(file.getAbsolutePath(), newFile.getAbsolutePath() + "/" + file.getName());
                    }
                }
                return status;
            }

        } else {
            return false;
        }
    }

    /**
     * 压缩文件夹
     *
     * @param sourcePath 要被压缩的文件夹路径
     * @param outPutPath 输出的文件夹路径，包含.zip后缀名
     * @return 压缩结果
     */
    public boolean compressToZip(@NotNull String sourcePath, @NotNull String outPutPath) {
        //源文件处理
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            JtlwLogUtil.logUtils.logI(TAG, sourcePath + "-zip压缩：源文件不存在");
            return false;
        }
        //输出文件处理
        File outputFile = new File(outPutPath);
        if (outputFile.exists()) {
            JtlwLogUtil.logUtils.logI(TAG, sourcePath + "-zip压缩：输出目标存在，不进行压缩");
            return false;
        }

        //输出流
        FileOutputStream outputStream = null;
        //压缩输出流
        ZipOutputStream zos = null;
        try {
            JtlwLogUtil.logUtils.logI(TAG, sourcePath + "-zip压缩：开始进行压缩:" + JtlwDateTimeUtil.getInstance().getMillisecond());
            outputStream = new FileOutputStream(new File(outPutPath));
            zos = new ZipOutputStream(outputStream);
            boolean status = compressToZip(sourceFile, zos, sourceFile.getName());
            if (status) {
                JtlwLogUtil.logUtils.logI(TAG, sourcePath + "-zip压缩：压缩完成:" + JtlwDateTimeUtil.getInstance().getMillisecond());
            } else {
                JtlwLogUtil.logUtils.logI(TAG, sourcePath + "-zip压缩：压缩失败");
            }
            return status;
        } catch (Exception e) {
            JtlwLogUtil.logUtils.logI(TAG, sourcePath + "-zip压缩：压缩失败，异常原因：" + (e.getMessage() != null ? e.getMessage() : ""));
            return false;
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ignored) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 私有压缩文件夹
     *
     * @param sourceFile   源文件
     * @param zos          压缩流
     * @param saveFileName 保存的文件名
     * @return 压缩结果
     */
    private boolean compressToZip(File sourceFile, ZipOutputStream zos, String saveFileName) {
        try {
            byte[] buf = new byte[2048];
            if (sourceFile.isFile()) {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(saveFileName));
                // copy文件到zip输出流中
                int len;
                FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            } else {
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(saveFileName + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                } else {
                    boolean status = true;
                    for (File file : listFiles) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        status = status && compressToZip(file, zos, saveFileName + "/" + file.getName());
                    }
                    return status;
                }
            }
            return true;
        } catch (Exception e) {
            JtlwLogUtil.logUtils.logI(TAG, sourceFile.getAbsolutePath() + "-zip压缩：压缩失败，异常原因：" + (e.getMessage() != null ? e.getMessage() : ""));
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param path 要删除的文件路径、
     * @return 删除结果
     */
    public boolean deleteFile(String path) {
        boolean result = false;
        File file = new File(path);
        if (file.exists()) {
            System.gc();
            result = file.delete();
        }
        return result;
    }

    /**
     * 获取文件大小，单位B
     *
     * @param file          文件地址
     * @param filtrationDir 过滤的地址
     * @return 文件大小
     */
    public Long getFileSize(File file, String filtrationDir) {
        long fileSize = 0L;
        if (file.isFile() && file.exists()) {
            fileSize += file.length();
        } else if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.isFile()) {
                        fileSize += file1.length();
                    } else if (file.isDirectory() || filtrationDir == null || !filtrationDir.equals(file.getAbsolutePath())) {
                        fileSize += getFileSize(file1, filtrationDir);
                    }
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
    public boolean deleteDirectory(String filePath) {
        boolean flag;
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
        if (files != null) {
            for (File childFile : files) {
                if (childFile.isFile()) {
                    //删除子文件
                    flag = deleteFile(childFile.getAbsolutePath());
                } else {
                    //删除子目录
                    flag = deleteDirectory(childFile.getAbsolutePath());
                }
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        } else {
            try {
                return dirFile.delete();
            } catch (Exception e) {
                return false;
            }
        }
        //删除当前空目录
    }

    /**
     * 获取绝对路径下最后一个文件夹名称
     *
     * @param absolutePath 文件夹绝对路径
     * @return 文件名称
     */
    public String getLastDirectoryName(String absolutePath) {
        if (absolutePath == null) {
            return "";
        }
        //创建新的，防止由于使用同一个对象对于调用该方法的值产生影响
        String path = absolutePath.intern();
        //判断是不是文件，是文件的话获取父文件夹路径
        File file = new File(path);
        if (file.isFile()) {
            path = file.getParentFile().getAbsolutePath();
        }

        if (path.contains("/")) {
            //循环移除末尾的“/”，防止一个路径下有多个“/”
            while ("/".equals(path.substring(path.lastIndexOf("/")).intern())) {
                path = path.substring(0, path.length() - 1);
            }
            path = path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }

    /**
     * 创建文件夹
     *
     * @param path          文件路径
     * @param nowPathIsFile 当前地址代表的是否是文件
     * @return 创建结果
     */
    public boolean createDirectory(String path, boolean nowPathIsFile) {
        //检测地址是否为空
        if (JtlwCheckVariateUtil.getInstance().isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        //先判断文件夹是否存在
        if (file.exists()) {
            return true;
        }
        //判断当前地址是否是文件，是文件的话则要对父级文件夹做创建
        if (nowPathIsFile) {
            file = file.getParentFile();
        }
        return file.mkdirs();
    }

    /**
     * 根据正则获取指定目录下的所有文件列表(使用递归扫描方式)
     *
     * @param scanPath     要扫描的问题件路径
     * @param matchRegular 文件正则
     * @return 文件列表
     */
    public List<File> getFileListForMatchRecursionScan(String scanPath, String matchRegular) {
        List<File> list = new ArrayList<>();
        if (!JtlwCheckVariateUtil.getInstance().isHaveEmpty(scanPath, matchRegular)) {
            File file = new File(scanPath);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        if (childFile != null) {
                            if (childFile.isDirectory()) {
                                if (!file.getName().matches("^\\..*")) {
                                    list.addAll(getFileListForMatchRecursionScan(childFile.getAbsolutePath(), matchRegular));
                                }
                            } else if (childFile.isFile()) {
                                if (childFile.getName().matches(matchRegular)) {
                                    list.add(childFile);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 根据正则获取指定目录下的所有文件列表(使用队列扫描方式)
     *
     * @param scanPath     要扫描的文件路径
     * @param matchRegular 要返回的文件的正则格式
     * @return 扫描到的文件列表
     */
    public synchronized List<File> getFileListForMatchLinkedQueueScan(String scanPath, final String matchRegular) {
        final List<File> list = new ArrayList<>();
        if (!JtlwCheckVariateUtil.getInstance().isHaveEmpty(scanPath, matchRegular)) {
            if (fileOptionsLinkedQueue == null) {
                fileOptionsLinkedQueue = new ConcurrentLinkedQueue<>();
            }
            File file = new File(scanPath);
            if (file.exists() && !file.isFile()) {
                //获取到根目录下的文件和文件夹
                final File[] files = file.listFiles((file1, s) -> {
                    //过滤掉隐藏文件
                    return !file1.getName().trim().startsWith(".");
                });
                //临时存储任务,便于后面全部投递到线程池
                List<Runnable> runnableList = new ArrayList<>();
                //创建信号量(最多同时有10个线程可以访问)
                final Semaphore semaphore = new Semaphore(100);
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            //把目录添加进队列
                            fileOptionsLinkedQueue.offer(f);
                            //创建的线程的数目是根目录下文件夹的数目
                            Runnable runnable = () -> {
                                //开启文件夹扫描，使用多线程异步扫描进行处理，只有当全部处理完成是返回
                                list.addAll(startFileScan(matchRegular));
                            };
                            runnableList.add(runnable);
                        } else if (f.isFile()) {
                            if (f.getName().matches(matchRegular)) {
                                list.add(f);
                            }
                        }
                    }
                }

                //固定数目线程池(最大线程数目为cpu核心数,多余线程放在等待队列中)
                final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                for (Runnable runnable : runnableList) {
                    executorService.submit(runnable);
                }
                //不允许再添加线程
                executorService.shutdown();
                //等待线程池中的所有线程运行完成
                while (!executorService.isTerminated()) {
                    try {
                        //休眠1s
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        fileOptionsLinkedQueue = null;
        return list;
    }

    /**
     * 获取指定文件夹下的所有文件信息，要扫描的文件夹从队列当中获取，不使用递归，使用队列处理，当检测到是文件夹的话则将文件夹加入到队列当中继续执行循环
     * 知道当前队列内的所有文件夹当中的所有文件都被取出记录
     *
     * @param matchRegular 要取出文件的正则
     * @return 符合正则的集合
     */
    private List<File> startFileScan(String matchRegular) {
        List<File> list = new ArrayList<>();
        //对目录进行层次遍历，知道队列内容全部执行完成
        while (!fileOptionsLinkedQueue.isEmpty()) {
            //队头出队列
            final File tmpFile = fileOptionsLinkedQueue.poll();
            final File[] fileArray = tmpFile.listFiles((file, s) -> {
                //过滤掉隐藏文件
                return !file.getName().trim().startsWith(".");
            });
            assert fileArray != null;
            for (File f : fileArray) {
                if (f != null) {
                    if (f.isDirectory()) {
                        //把目录添加进队列
                        fileOptionsLinkedQueue.offer(f);
                    } else {
                        if (f.getName().matches(matchRegular)) {
                            list.add(f);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 清理指定文件夹下所有的空文件夹
     *
     * @param dirPath  文件夹地址
     * @param callback 回调
     */
    public synchronized void clearEmptyFileDir(String dirPath, JtlwFileClearEmptyDirCallback callback) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(dirPath)) {
            File file = new File(dirPath);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                if (files == null || files.length == 0 || (files.length == 1 && ".nomedia".equals(files[0].getName()))) {
                    final String path = file.getAbsolutePath();
                    boolean status = deleteDirectory(path);
                    if (callback != null) {
                        callback.currentEmptyFileDir(path, status);
                    }
                } else {
                    for (File childFile : files) {
                        if (childFile.exists() && childFile.isDirectory()) {
                            clearEmptyFileDir(childFile.getAbsolutePath(), callback);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public JtlwFileTypeEnum getFileType(String filePath) {
        try {
            return getFileType(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            return JtlwFileTypeEnum.OTHER;
        }
    }

    /**
     * 获取文件类型
     *
     * @param inputStream 输入流
     * @return 返回类型
     */
    public JtlwFileTypeEnum getFileType(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return null;
            } else {
                byte[] bytes = new byte[28];
                inputStream.read(bytes, 0, bytes.length);
                String fileHeader = JtlwCommonUtil.getInstance().bytesToHexString(bytes).toLowerCase();
                for (JtlwFileTypeEnum type : JtlwFileTypeEnum.values()) {
                    if (fileHeader.startsWith(type.getStart().toLowerCase())) {
                        return type;
                    }
                }
                return JtlwFileTypeEnum.OTHER;
            }
        } catch (Exception e) {
            return JtlwFileTypeEnum.OTHER;
        }
    }

    /**
     * 获取文件编码格式
     *
     * @param filePath 文件地址
     * @return 编码格式
     */
    public Charset getFileCodedFormat(String filePath) {
        String javaEncode = EncodingDetect.getJavaEncode(filePath);
        switch (javaEncode.toLowerCase()) {
            case "utf-8":
                return StandardCharsets.UTF_8;
            case "unicode":
                return Charset.forName("unicode");
            default:
                return Charset.forName("gbk");
        }
    }

    /**
     * 修改文件编码格式
     *
     * @param filePath       文件地址
     * @param oldCodedFormat 旧文件编码格式
     * @param newCodedFormat 新文件的编码格式
     * @return 是否成功，成功返回true
     */
    public boolean changeFileCodedFormat(String filePath, Charset oldCodedFormat, Charset newCodedFormat) {
        //读取文件原内容
        String content = readFileContent(filePath, oldCodedFormat);
        //写入新内容
        return writeFilContent(filePath, newCodedFormat, new String(content.getBytes(newCodedFormat), newCodedFormat));
    }

    /**
     * 以指定编码方式读取文件，返回文件内容
     *
     * @param filePath    要转换的文件
     * @param codedFormat 源文件的编码
     * @return 文件内容
     */
    public String readFileContent(String filePath, Charset codedFormat) {
        byte[] bytes = readBytes(filePath);
        if (bytes == null) {
            return "";
        } else {
            return new String(bytes, codedFormat);
        }
    }

    /**
     * 以指定编码方式写文本文件，存在会覆盖
     *
     * @param filePath      要写入的文件
     * @param toCharsetName 要转换的编码
     * @param content       文件内容
     * @return 是否成功
     */
    public boolean writeFilContent(String filePath, Charset toCharsetName, String content) {
        //先删除文件
        deleteFile(filePath);

        OutputStream outputStream = null;
        OutputStreamWriter outWrite = null;
        try {
            outputStream = new FileOutputStream(filePath);
            outWrite = new OutputStreamWriter(outputStream, toCharsetName);
            outWrite.write(content);
            outWrite.flush();
            outputStream.flush();
            return true;
        } catch (Exception e) {
            System.out.print(e.getClass() + "\n");
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outWrite != null) {
                try {
                    outWrite.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 所有文档类型枚举
     */
    private final List<JtlwFileTypeEnum> allDocFileTypeEnum;
    /**
     * 所有图片类型枚举
     */
    private final List<JtlwFileTypeEnum> allImageFileTypeEnum = new ArrayList<>();

    /**
     * 获取所有文档相关类型
     *
     * @return 返回相应的文档类型集合
     */
    public List<JtlwFileTypeEnum> getDocType() {
        if (allDocFileTypeEnum.isEmpty()) {
            synchronized (allDocFileTypeEnum) {
                if (allDocFileTypeEnum.isEmpty()) {
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.DOC);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.XLS);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.PDF);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.DOCX);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.XLSX);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.MDB);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.PST);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.DBX);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.XLSX_DOCX);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.XLS_DOC);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.VSD);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.WPS);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.WPD);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.EPS);
                    allDocFileTypeEnum.add(JtlwFileTypeEnum.TXT);
                }
            }
        }
        return allDocFileTypeEnum;
    }

    /**
     * 获取所有图片的相关类型
     *
     * @return 所有图片相关类型
     */
    public List<JtlwFileTypeEnum> getImageType() {
        if (allImageFileTypeEnum.isEmpty()) {
            synchronized (allImageFileTypeEnum) {
                if (allImageFileTypeEnum.isEmpty()) {
                    allImageFileTypeEnum.add(JtlwFileTypeEnum.JPG);
                    allImageFileTypeEnum.add(JtlwFileTypeEnum.JPEG);
                    allImageFileTypeEnum.add(JtlwFileTypeEnum.PNG);
                    allImageFileTypeEnum.add(JtlwFileTypeEnum.BMP);
                    allImageFileTypeEnum.add(JtlwFileTypeEnum.GIF);
                    allImageFileTypeEnum.add(JtlwFileTypeEnum.TIF);
                }
            }
        }
        return allImageFileTypeEnum;
    }

    /**
     * 重命名文件
     *
     * @param oldFile     旧文件
     * @param newFileName 新文件名称
     * @return 重命名结果
     */
    public boolean renameFile(File oldFile, String newFileName) {
        if (oldFile != null && oldFile.exists()) {
            File newFile = new File(oldFile.getParentFile().getAbsolutePath() + "/" + newFileName);
            return oldFile.renameTo(newFile);
        }
        return false;
    }

    /**
     * 添加文字水印
     *
     * @param srcPath   原始图片地址
     * @param outPath   输出文件地址
     * @param locationX 水印x轴坐标
     * @param locationY 水印y轴坐标
     * @param text      水印文本
     * @param textColor 水印颜色
     */
    public void addTextWaterMark(@NotNull String srcPath, @NotNull String outPath, int locationX, int locationY, @NotNull String text,
            @NotNull Color textColor) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(text)) {
            //创建文件夹
            createDirectory(outPath, true);
            // 读取原图片信息
            try {
                File srcImgFile = new File(srcPath);
                Image srcImg = ImageIO.read(srcImgFile);
                int srcImgWidth = srcImg.getWidth(null);
                int srcImgHeight = srcImg.getHeight(null);
                // 加水印
                BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bufImg.createGraphics();
                g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
                // Font font = new Font("Courier New", Font.PLAIN, 12);
                Font font = new Font("宋体", Font.PLAIN, 40);
                //根据图片的背景设置水印颜色
                g.setColor(textColor);
                g.setFont(font);
                g.drawString(text, locationX, locationY);
                g.dispose();
                // 输出图片
                FileOutputStream outImgStream = new FileOutputStream(outPath);
                ImageIO.write(bufImg, outPath.substring(outPath.lastIndexOf(".") + 1), outImgStream);
                outImgStream.flush();
                outImgStream.close();
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, "文字水印添加失败：" + e.getMessage());
            }
        }
    }

    /**
     * 添加文字水印
     *
     * @param srcPath          原始图片地址
     * @param outPath          输出文件地址
     * @param locationPercentX 水印x轴坐标百分比
     * @param locationPercentY 水印y轴坐标百分比
     * @param text             水印文本
     * @param textColor        水印颜色
     */
    public void addTextWaterMark(@NotNull String srcPath, @NotNull String outPath, float locationPercentX, float locationPercentY,
            @NotNull String text, @NotNull Color textColor) {
        if (!JtlwCheckVariateUtil.getInstance().isEmpty(text)) {
            //创建文件夹
            createDirectory(outPath, true);
            // 读取原图片信息
            try {
                File srcImgFile = new File(srcPath);
                Image srcImg = ImageIO.read(srcImgFile);
                int srcImgWidth = srcImg.getWidth(null);
                int srcImgHeight = srcImg.getHeight(null);
                addTextWaterMark(srcPath, outPath, (int) (srcImgWidth * locationPercentX), (int) (srcImgHeight * locationPercentY), text, textColor);
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, "文字水印添加失败：" + e.getMessage());
            }
        }
    }


}

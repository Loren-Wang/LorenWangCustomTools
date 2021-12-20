package com.lorenwang.test.android.activity.customToolsAndroid

import android.Manifest
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.app.AtlwThreadUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.os.Bundle
import android.util.Xml
import android.view.View
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsAndroidFileBinding
import javabase.lorenwang.tools.file.JtlwFileOptionUtil
import java.io.File

/**
 * 功能作用：文件工具类型相关
 * 初始注释时间： 2021/9/26 10:42
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
class FileActivity : BaseActivity() {

    private lateinit var binding: ActivityCustomToolsAndroidFileBinding


    override fun initView(savedInstanceState: Bundle?) {
        binding = ActivityCustomToolsAndroidFileBinding.inflate(layoutInflater)
        addShowContentView(true, binding)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        AtlwThreadUtil.getInstance().postOnChildThread {
            val files = File(AtlwFileOptionUtil.getInstance().baseStorageDirPath + "/Android").listFiles()
            if (files != null) {
                for (file in files) {
                    appendText("${
                        JtlwFileOptionUtil.getInstance().paramsFileSize(JtlwFileOptionUtil.getInstance().getFileSize(file, null))
                    }-${file.absolutePath}\n")
                }
            }
        }
    }

    fun mainClick(view: View?) {
        if (view != null) {
            binding.tvShow.text = ""
            //app文件夹地址
            val appSystemStorageDirPath = AtlwFileOptionUtil.getInstance().getAppSystemStorageDirPath(BuildConfig.APPLICATION_ID)
            //内容文本
            val content = binding.etContent.text.toString()
            //手机系统存储根目录
            val baseStorageDirPath = AtlwFileOptionUtil.getInstance().baseStorageDirPath
            AtlwActivityUtil.getInstance()
                .goToRequestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    object : AtlwPermissionRequestCallback {
                        override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?) {
                            when (view.id) {
                                //清除空文件夹
                                R.id.btnClearEmptyDir -> {
                                    AtlwThreadUtil.getInstance().postOnChildThread {
                                        appendText("开始清除...\n")
                                        JtlwFileOptionUtil.getInstance().clearEmptyFileDir(baseStorageDirPath) { filePath, status ->
                                            appendText("${status}-${filePath}\n")
                                        }
                                        appendText("清除结束\n")
                                    }
                                }
                                //清除apk
                                R.id.btnClearApk -> {
                                    AtlwThreadUtil.getInstance().postOnChildThread {
                                        appendText("开始扫描...\n")
                                        val list = JtlwFileOptionUtil.getInstance().getFileListForMatchRecursionScan(baseStorageDirPath, ".*\\.apk$")
                                        appendText("扫描结束，开始清除，扫描到文件大小${list.size}\n")
                                        for (file in list) {
                                            appendText("${file.delete()}-${file.absolutePath}\n")
                                        }
                                        appendText("清除完成\n")
                                        AtlwToastHintUtil.getInstance().toastMsg("apk清理结束")
                                    }
                                }
                                //清除log
                                R.id.btnClearLog -> {
                                    AtlwThreadUtil.getInstance().postOnChildThread {
                                        appendText("开始扫描...\n")
                                        val list = JtlwFileOptionUtil.getInstance()
                                            .getFileListForMatchRecursionScan(baseStorageDirPath, "(.*\\.log\$)|(.*Log\\.txt\$)")
                                        appendText("扫描结束，开始清除，扫描到文件大小${list.size}\n")
                                        for (file in list) {
                                            appendText("${file.delete()}-${file.absolutePath}\n")
                                        }
                                        appendText("清除完成\n")
                                        AtlwToastHintUtil.getInstance().toastMsg("log清理结束")
                                    }
                                }
                                //清除音频
                                R.id.btnClearAudio -> {
                                    AtlwThreadUtil.getInstance().postOnChildThread {
                                        appendText("开始扫描...\n")
                                        val list = JtlwFileOptionUtil.getInstance().getFileListForMatchRecursionScan(baseStorageDirPath, ".*\\.mp3$")
                                        appendText("扫描结束，开始清除，扫描到文件大小${list.size}\n")
                                        for (file in list) {
                                            appendText("${file.delete()}-${file.absolutePath}\n")
                                        }
                                        appendText("清除完成\n")
                                        AtlwToastHintUtil.getInstance().toastMsg("音频清理结束")
                                    }
                                }
                                //向文件写入内容
                                R.id.btnWriteToFile -> {
                                    val file = File(appSystemStorageDirPath + "test.txt")
                                    if (content.isNotEmpty()) {
                                        if (AtlwFileOptionUtil.getInstance().writeToFile(true, file, content)) {
                                            appendText("写入成功,文件地址：${file}\n")
                                            appendText("当前文件内容：${String((AtlwFileOptionUtil.getInstance().readBytes(true, file)))}")
                                        } else {
                                            appendText("写入失败")
                                        }
                                    } else {
                                        AtlwToastHintUtil.getInstance().toastMsg("请输入内容")
                                    }
                                }
                                //向文件写入内容拼接在后面
                                R.id.btnWriteToFileAppend -> {
                                    val file = File(appSystemStorageDirPath + "test.txt")
                                    if (content.isNotEmpty()) {
                                        if (AtlwFileOptionUtil.getInstance().writeToFile(true, file, content, Xml.Encoding.UTF_8.toString(), true)) {
                                            appendText("追加写入成功,文件地址：${file}\n")
                                            appendText("当前文件内容：${String((AtlwFileOptionUtil.getInstance().readBytes(true, file)))}")
                                        } else {
                                            appendText("追加写入失败")
                                        }
                                    } else {
                                        AtlwToastHintUtil.getInstance().toastMsg("请输入内容")
                                    }
                                }
                                //复制文件
                                R.id.btnCopy -> {
                                    val file = File(appSystemStorageDirPath + "test.txt")
                                    val newFile = File(appSystemStorageDirPath + (Math.random() * 100).toString() + ".txt")
                                    if (AtlwFileOptionUtil.getInstance().copyFile(true, file.absolutePath, newFile.absolutePath)) {
                                        appendText("文件复制成功，新文件地址：${newFile.absolutePath}")
                                    } else {
                                        appendText("文件复制失败")
                                    }
                                }
                                //创建文件夹
                                R.id.btnCreateDir -> {
                                    val newFile = File(appSystemStorageDirPath + (Math.random() * 100).toString())
                                    if (AtlwFileOptionUtil.getInstance().createDirectory(true, newFile.absolutePath, newFile.isFile)) {
                                        appendText("文件夹创建成功，新文件夹地址：${newFile.absolutePath}")
                                    } else {
                                        appendText("文件夹创建失败")
                                    }
                                }
                                //文件存储根目录
                                R.id.btnStorage -> {
                                    appendText(baseStorageDirPath)
                                }
                                //获取App存储根目录
                                R.id.btnAppStorage -> {
                                    appendText(appSystemStorageDirPath)
                                }
                                //获取app缓存文件大小
                                R.id.btnAppCache -> {
                                    val size = AtlwFileOptionUtil.getInstance().getAppCacheFileSize(true)
                                    val paramsSize = JtlwFileOptionUtil.getInstance().paramsFileSize(size)
                                    appendText("当前缓存文件大小：${size}B\n")
                                    appendText("当前缓存文件转换后大小：${paramsSize}B\n")
                                }
                                //清除app文件缓存
                                R.id.btnClearAppCache -> {
                                    if (AtlwFileOptionUtil.getInstance().clearAppCacheFile(true)) {
                                        appendText("当前缓存文件清除成功")
                                        val size = AtlwFileOptionUtil.getInstance().getAppCacheFileSize(true)
                                        val paramsSize = JtlwFileOptionUtil.getInstance().paramsFileSize(size)
                                        appendText("当前缓存文件大小：${size}B\n")
                                        appendText("当前缓存文件转换后大小：${paramsSize}B\n")
                                    } else {
                                        appendText("当前缓存文件清除失败")
                                    }
                                }
                                else -> {

                                }
                            }
                        }

                        override fun permissionRequestFailCallback(permissionList: MutableList<String>?) {

                        }
                    })

        }
    }

    /**
     * 拼接字符串
     */
    private fun appendText(text: String) {
        runOnUiThread {
            binding.tvShow.append(text)
        }
    }
}

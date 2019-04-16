package com.example.testapp

import android.Manifest
import android.lorenwang.tools.app.ActivityUtils
import android.lorenwang.tools.app.PermissionRequestCallback
import android.lorenwang.tools.app.ThreadUtils
import android.lorenwang.tools.file.AtlwFileOptionUtils
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.testapp.adapter.FileScanAdapter
import java.io.File
import java.util.*

/**
 * 创建时间：2019-04-15 下午 14:59:7
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class FileOptionsForScanFileActivity : BaseActivity(), PermissionRequestCallback {

    private lateinit var recyList: RecyclerView
    private val data = ArrayList<File>()
    private var adapter: FileScanAdapter? = null
    private val files = ArrayList<File>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_options_for_scan)
        recyList = findViewById(R.id.rv_scan_result)
        val manager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        recyList.setLayoutManager(manager)
        adapter = FileScanAdapter(this)
        adapter?.setOnCheckListener({ file ->
            if (file.isDirectory()) {
//                upDataFileListForDir(file.getAbsolutePath())
            } else {
                if (!data.contains(file))
                    data.add(file)
            }
        })
        adapter?.setData(files)
        recyList.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        showLoading()
        ActivityUtils.getInstance().goToRequestPermisstions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0, this)
    }

    override fun perissionRequestSuccessCallback(perissionList: MutableList<String>?, permissionsRequestCode: Int) {
        files.clear()
        ThreadUtils.getInstance().postOnChildThread {
            files.addAll(AtlwFileOptionUtils.getInstance().getFileListForMatchLinkedQueueScan(applicationContext,true, Environment.getExternalStorageDirectory().getPath(), "^[^\\.].*\\.txt$"));
            ThreadUtils.getInstance().postOnUiThread {
                adapter?.setData(files);
                hideLoading()
            }
        }
    }

    override fun perissionRequestFailCallback(perissionList: MutableList<String>?, permissionsRequestCode: Int) {
    }

}

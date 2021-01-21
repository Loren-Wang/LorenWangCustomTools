package com.example.testapp.activity

import android.Manifest
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.app.AtlwThreadUtil
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.adapter.FileScanAdapter
import com.example.testapp.base.BaseActivity
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

class FileOptionsForScanFileActivity : BaseActivity(), AtlwPermissionRequestCallback {

    private lateinit var recyList: RecyclerView
    private val data = ArrayList<File>()
    private var adapter: FileScanAdapter? = null
    private val files = ArrayList<File>()

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_file_options_for_scan)
        recyList = findViewById(R.id.rv_scan_result)
        val manager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        recyList.layoutManager = manager
        adapter = FileScanAdapter(this)
        adapter?.setOnCheckListener { file ->
            if (file.isDirectory) {
//                upDataFileListForDir(file.getAbsolutePath())
            } else {
                if (!data.contains(file))
                    data.add(file)
            }
        }
        adapter?.setData(files)
        recyList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        showBaseLoading(false)
        AtlwActivityUtil.getInstance().goToRequestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0, this)
    }

    override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
        files.clear()
        AtlwThreadUtil.getInstance().postOnChildThread {
            files.addAll(
                AtlwFileOptionUtil.getInstance().getFileListForMatchLinkedQueueScan( true, Environment.getExternalStorageDirectory().path, "^[^\\.].*\\.txt$"))
            AtlwThreadUtil.getInstance().postOnUiThread {
                adapter?.setData(files)
                hideBaseLoading()
            }
        }
    }

    override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
    }

}

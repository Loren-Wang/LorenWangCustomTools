package com.example.testapp.activity

import android.lorenwang.tools.file.AtlwFileOptionUtils
import android.os.Bundle
import android.widget.TextView
import com.example.testapp.BuildConfig
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

class FileOptionsActivity : BaseActivity() {

    private var tvFileBasePath: TextView? = null
    private var tvFileAppSystemPath: TextView? = null

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_file_options)

        tvFileBasePath = findViewById(R.id.tvFileBasePath)
        tvFileAppSystemPath = findViewById(R.id.tvFileAppSystemPath)

        tvFileBasePath?.setText(AtlwFileOptionUtils.getInstance().baseStorageDirPath)
        tvFileAppSystemPath?.setText(AtlwFileOptionUtils.getInstance().getAppSystemStorageDirPath(BuildConfig.APPLICATION_ID))
    }
}

package com.example.testapp

import android.lorenwang.tools.file.AtlwFileOptionUtils
import android.os.Bundle
import android.widget.TextView

class FileOptionsActivity : BaseActivity() {

    private var tvFileBasePath: TextView? = null
    private var tvFileAppSystemPath: TextView? = null

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_file_options)

        tvFileBasePath = findViewById(R.id.tvFileBasePath)
        tvFileAppSystemPath = findViewById(R.id.tvFileAppSystemPath)

        tvFileBasePath?.setText(AtlwFileOptionUtils.getInstance().baseStorageDirPath)
        tvFileAppSystemPath?.setText(AtlwFileOptionUtils.getInstance().getAppSystemStorageDirPath(BuildConfig.APPLICATION_ID))
    }
}

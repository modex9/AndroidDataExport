package com.example.myfirstapplication.export

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import androidx.annotation.RequiresApi

class CallLogsExport : Export() {
    override var permission : String = Manifest.permission.READ_CALL_LOG
    override var filename : String = "call_log.json"
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override var contentUri : Uri = CallLog.Calls.CONTENT_URI
    override var successMessage: String = "Call logs were successfully exported."
    override var REQUEST_PERMISSION = 3
}
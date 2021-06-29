package com.example.myfirstapplication.export

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi

class ThreadsExport : Export() {
    override var permission : String = Manifest.permission.READ_SMS
    override var filename : String = "threads.json"
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override var contentUri : Uri = Telephony.Threads.CONTENT_URI
    override var successMessage: String = "Threads were successfully exported."
    override var REQUEST_PERMISSION = 1
}
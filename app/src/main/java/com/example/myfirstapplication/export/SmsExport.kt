package com.example.myfirstapplication.export

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi

class SmsExport : Export() {
    override var permission : String = Manifest.permission.READ_SMS
    override var filename : String = "sms.json"
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override var contentUri : Uri = Telephony.Sms.CONTENT_URI
    override var successMessage: String = "Sms messages were successfully exported."
    override var REQUEST_PERMISSION = 2
}
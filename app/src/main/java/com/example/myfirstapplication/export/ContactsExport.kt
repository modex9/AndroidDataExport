package com.example.myfirstapplication.export

import android.Manifest
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.RequiresApi

class ContactsExport: Export() {
    override var permission : String = Manifest.permission.READ_CONTACTS
    override var filename : String = "contacts.json"
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override var contentUri : Uri = ContactsContract.Contacts.CONTENT_URI
    override var successMessage: String = "Contacts were successfully exported."
    override var REQUEST_PERMISSION = 4
}
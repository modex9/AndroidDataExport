package com.example.myfirstapplication.export

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myfirstapplication.MainActivity
import com.example.myfirstapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


open abstract class Export {

    protected open var permission : String = "";
    protected open var filename : String = "";
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    protected open var contentUri : Uri = Telephony.Sms.CONTENT_URI;
    protected open var successMessage : String = "Data were successfully exported."
    protected open var REQUEST_PERMISSION = 0

    private fun writeToFile(context : Context, data: String) {

        try {
            val outputStreamWriter =
                OutputStreamWriter(
                    FileOutputStream(
                        File(
                            context.getExternalFilesDir(null),
                            filename
                        )
                    )
                )
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    protected fun checkPermission(context : Context): Boolean
    {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    protected fun getCursor(contentResolver : ContentResolver): Cursor? {
        var cursor = contentResolver.query(
            contentUri,  // The content URI of the words table
            null,
            null,
            null,
            null
        )
        return cursor;
    }

    protected fun exportJson(context : Context, contentResolver : ContentResolver)
    {
        val cursor = getCursor(contentResolver)
        var dataCollection = HashMap<Int, HashMap<String, String>>()

        var counter = 0;
        cursor?.moveToFirst()
        while (cursor?.isAfterLast == false)
        {
            var data = HashMap<String, String>()
            // Collect all threads data
            var columnIterator = cursor?.columnNames?.iterator()
            columnIterator?.forEach { it ->
                var value: String? = cursor.getString(cursor.getColumnIndex(it))
                data[it] = value ?: "null"
            }
            dataCollection[counter] = data
            counter++
            cursor.moveToNext()
        }

        val gson = Gson()
        val threadsJson = gson.toJson(dataCollection).toString()
        this.writeToFile(context, threadsJson)
    }

    fun getColumnData(contentResolver: ContentResolver, columnName: String): ArrayList<String> {
        val cursor = getCursor(contentResolver)
        var columnId = cursor?.getColumnIndex(columnName)
        cursor?.moveToFirst()
        var columnData = ArrayList<String>()
        while (cursor?.isAfterLast == false)
        {
            var data = cursor.getString(columnId!!)
            if(!data.isNullOrBlank())
                columnData.add(data)
            cursor.moveToNext()
        }
        return columnData
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun export(context : Context, contentResolver : ContentResolver) {
        val activity = context as MainActivity
        if(checkPermission(context))
        {
            exportJson(context, contentResolver)
            Snackbar.make(
                activity.findViewById(R.id.myCoordinatorLayout),
                successMessage,
                Snackbar.LENGTH_SHORT
            ).show()
        }
        else
        {
            activity.requestPermissions(arrayOf(permission), this.REQUEST_PERMISSION)
        }
    }
}
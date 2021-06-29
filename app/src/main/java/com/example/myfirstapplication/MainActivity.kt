package com.example.myfirstapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapplication.export.CallLogsExport
import com.example.myfirstapplication.export.Export
import com.example.myfirstapplication.export.SmsExport
import com.example.myfirstapplication.export.ThreadsExport
import com.google.android.material.snackbar.Snackbar

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {

    val REQUEST_PERMISSION_THREADS = 1
    val REQUEST_PERMISSION_SMS = 2
    val REQUEST_PERMISSION_CALL_LOG = 3

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            //Catch your exception
            // Without System.exit() this will not work.
            paramThrowable.printStackTrace();
            System.exit(2)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    /** Called when user exports data */
    fun showExortSnackbar(view: View)
    {
        Snackbar.make(
            findViewById(R.id.myCoordinatorLayout),
            "test",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun exportData(view: View)
    {
        // Instantiate export object according to button tag. Todo : factory?
        val tag = view.tag
        var exportObject : Export? = null
        when (tag)
        {
            ("threads") -> {
                exportObject = ThreadsExport()
            }
            ("sms") -> {
                exportObject = SmsExport()
            }
            ("call_logs") -> {
                exportObject = CallLogsExport()
            }
        }

        if(exportObject !== null)
        {
            exportObject.export(this, contentResolver)
        }
        else
        {
            Snackbar.make(
                findViewById(R.id.myCoordinatorLayout),
                "Failed to export data.",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    // If permissions are not already granted, execute exports immediately after permission is granted.
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var exportObject : Export? = null
        when (requestCode) {
            REQUEST_PERMISSION_THREADS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    exportObject = ThreadsExport()
                }
            }
            REQUEST_PERMISSION_SMS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    exportObject = SmsExport()
                }
            }
            REQUEST_PERMISSION_CALL_LOG -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    exportObject = CallLogsExport()
                }
            }
        }
        if(exportObject !== null)
        {
            exportObject.export(this, contentResolver)
        }
        else
        {
            Snackbar.make(
                findViewById(R.id.myCoordinatorLayout),
                "Failed to export data.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}
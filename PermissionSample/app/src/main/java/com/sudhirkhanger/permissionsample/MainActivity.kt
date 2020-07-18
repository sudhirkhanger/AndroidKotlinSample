package com.sudhirkhanger.permissionsample

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_MULTI_PERMISSIONS = 1000
        private val MULTI_PERMISSIONS_LIST = arrayOf(WRITE_EXTERNAL_STORAGE, CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startPermissionRequest()
            } else {
                messageDialog("Permission Granted", PermDialogEnum.MESSAGE)?.show()
            }
        }
    }

    private fun isPermissionGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldShowRationale(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(permission)) return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startPermissionRequest() {
        if (!isPermissionGranted(MULTI_PERMISSIONS_LIST)) {
            if (shouldShowRationale(MULTI_PERMISSIONS_LIST)) {
                messageDialog("Previously Denied. Show rationale", PermDialogEnum.REQUEST)?.show()
            } else {
                requestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
            }
        } else {
            messageDialog("Permission Granted", PermDialogEnum.MESSAGE)?.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_MULTI_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults.sum() == PackageManager.PERMISSION_GRANTED)) {
                    messageDialog("Permission Granted", PermDialogEnum.MESSAGE)?.show()
                } else if (!shouldShowRationale(MULTI_PERMISSIONS_LIST)) {
                    messageDialog("Don't ask again. Open settings", PermDialogEnum.SETTINGS)?.show()
                }
                return
            }
            else -> {

            }
        }
    }

    private enum class PermDialogEnum {
        MESSAGE,
        REQUEST,
        SETTINGS
    }

    private fun messageDialog(message: String, action: PermDialogEnum): AlertDialog? {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Permissions")
            setMessage(message)
            setPositiveButton("Ok") { _, _ -> }
            when (action) {
                PermDialogEnum.REQUEST -> {
                    setNegativeButton("Request") { _, _ ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            requestPermissions(
                                MULTI_PERMISSIONS_LIST,
                                REQUEST_CODE_MULTI_PERMISSIONS
                            )
                    }
                }
                PermDialogEnum.SETTINGS -> {
                    setNegativeButton("Open Settings") { _, _ ->
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:$packageName")
                        )
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                else -> {
                }
            }
            return builder.create()
        }
    }
}

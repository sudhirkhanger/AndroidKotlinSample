package com.sudhirkhanger.permissionsample

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

fun AppCompatActivity.isPermissionGranted(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.shouldShowPermissionRationale(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.batchRequestPermissions(permissions: Array<String>, requestId: Int) =
    ActivityCompat.requestPermissions(this, permissions, requestId)

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_MULTI_PERMISSIONS = 1000
        val MULTI_PERMISSIONS_LIST = arrayOf(WRITE_EXTERNAL_STORAGE, CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { startPermissionRequest() }
    }

    private fun startPermissionRequest() {
        if (!isPermissionGranted(WRITE_EXTERNAL_STORAGE) || !isPermissionGranted(CAMERA)) {
            if (shouldShowPermissionRationale(WRITE_EXTERNAL_STORAGE) || shouldShowPermissionRationale(CAMERA)) {
                showSnackBar("denied previously. show rationale")
                    .setAction("Request") {
                        batchRequestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
                    }
                    .show()
            } else {
                batchRequestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
            }
        } else {
            showSnackBar("granted").show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_MULTI_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    showSnackBar("Perform Action: Granted").show()
                } else {
                    if (shouldShowPermissionRationale(WRITE_EXTERNAL_STORAGE) || shouldShowPermissionRationale(CAMERA)) {
                        showSnackBar("Can't Perform Action: Denied.").show()
                    } else {
                        showSnackBar("denied. don't ask again. open settings").show()
                    }
                }
                return
            }

            else -> {

            }
        }
    }

    private fun showSnackBar(message: String) =
        Snackbar.make(findViewById<CoordinatorLayout>(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT)
}

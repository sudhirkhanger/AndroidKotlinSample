package com.sudhirkhanger.permissionsample

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
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
        private val MULTI_PERMISSIONS_LIST = arrayOf(WRITE_EXTERNAL_STORAGE, CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { startPermissionRequest() }
    }

    private fun startPermissionRequest() {
        /**
         * If permission is granted then carry on. if permission is not granted then use
         * shouldShowRequestPermissionRationale to figure out if the request has been previously denied, if
         * don't ask again is enabled, or if the permission is being asked for the first time.
         */
        if (!isPermissionGranted(WRITE_EXTERNAL_STORAGE) || !isPermissionGranted(CAMERA)) {

            /**
             * if permission has not been granted then show the rationale
             * shouldShowRequestPermissionRationale throws true if permission has been denied previously.
             * throws false in following two cases
             * 1. permission is being asked for the first time in which case you should just ask for permission.
             * 2. don't ask again is enabled
             */
            if (shouldShowPermissionRationale(WRITE_EXTERNAL_STORAGE) || shouldShowPermissionRationale(CAMERA)) {
                showSnackBar("Previously denied. Show rationale")
                    .setAction("Request") {
                        batchRequestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
                    }
                    .show()
            } else {
                /**
                 * this is shown only first time when the user has not had the opportunity to select
                 * don't ask again
                 * perm is requested only the first time.
                 */
                batchRequestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
            }
        } else {
            // Granted
            openFragTestActivity()
        }
    }

    /**
     * triggered as a result of requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_MULTI_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted
                    openFragTestActivity()
                } else {
                    /**
                     * if permission is not granted then we can check if the user has enabled don't ask again
                     * true - previously denied. ask asynchronously
                     * false - don't ask again checked. you may want to show option to enable settings.
                     */
                    if (shouldShowPermissionRationale(WRITE_EXTERNAL_STORAGE) || shouldShowPermissionRationale(CAMERA)) {
                        showSnackBar("Can't Perform Action: Previously denied")
                            .setAction("Request") {
                                batchRequestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
                            }
                            .show()
                    } else {
                        showSnackBar("Denied. Don't ask again. open settings").show()
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

    private fun openFragTestActivity() {
        startActivity(Intent(this, FragTestActivity::class.java))
    }
}

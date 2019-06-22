package com.sudhirkhanger.permissionsample

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class FragTestActivityFragment : Fragment(), View.OnClickListener {

    companion object {
        private const val REQUEST_CODE_MULTI_PERMISSIONS = 1001
        private val MULTI_PERMISSIONS_LIST = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE
        )
    }

    private lateinit var fragPermBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frag_test, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        fragPermBtn = view.findViewById(R.id.frag_perm_btn)
        fragPermBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == fragPermBtn) {
            startPermissionRequest(v.context)
        }
    }

    private fun startPermissionRequest(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE)
            ) {
                showSnackBar("Previously denied. Show rationale")
                    .setAction("Request") {
                        requestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
                    }
                    .show()
            } else {
                requestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
            }
        } else {
            showSnackBar("startPermissionRequest Granted").show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_MULTI_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted
                    showSnackBar("onRequestPermissionsResult Granted").show()
                } else {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                        shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE)
                    ) {
                        showSnackBar("Can't Perform Action: Previously denied")
                            .setAction("Request") {
                                requestPermissions(MULTI_PERMISSIONS_LIST, REQUEST_CODE_MULTI_PERMISSIONS)
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

    private fun showSnackBar(message: String) = Snackbar.make(fragPermBtn, message, Snackbar.LENGTH_SHORT)
}

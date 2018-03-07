package com.sudhirkhanger.timepickerdialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
    }

    fun openTimePicker(view: View) {
        var calendar = Calendar.getInstance()

        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
            kotlin.run {
                calendar = Calendar.getInstance()
                calendar.set(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        hour,
                        min,
                        calendar.get(Calendar.SECOND))
                val simpleDateFormatter = SimpleDateFormat("hh:mm:ssa", Locale.getDefault())
                val date = simpleDateFormatter.format(Date(calendar.timeInMillis))
                Log.e(TAG, "$date ${calendar.timeInMillis}")
                textView.text = "$date ${calendar.timeInMillis}"
            }
        }

        val timePickerDialog = TimePickerDialog(
                this,
                timePickerListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false)
        timePickerDialog.show()
    }
}

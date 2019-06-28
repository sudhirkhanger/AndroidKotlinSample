package com.sudhirkhanger.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var printBroadcastReceiver: BroadcastReceiver
    private lateinit var builder: NotificationCompat.Builder

    companion object {
        private const val BROADCAST_TIMBER = BuildConfig.APPLICATION_ID + ".PRINT_TEXT"
        private const val TAG = "MainActivity"
        private const val NOTIFICATION_ID = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createNotificationChannel()
        displayNotification()

        fab.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                cancel(NOTIFICATION_ID)
            }
        }

        printBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    BROADCAST_TIMBER -> Log.e(TAG, BROADCAST_TIMBER)
                }
            }
        }
        registerReceiver(printBroadcastReceiver, IntentFilter(BROADCAST_TIMBER))
    }

    override fun onDestroy() {
        unregisterReceiver(printBroadcastReceiver)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val mainActivityPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val printLogIntent = Intent(BROADCAST_TIMBER)
        val pIntent = PendingIntent.getBroadcast(this, 0, printLogIntent, 0)

        builder = NotificationCompat.Builder(this, "channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title")
            .setUsesChronometer(true)
            .setOngoing(true)
            .setContentText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(mainActivityPendingIntent)
            .setProgress(100, 0, false)
            .setAutoCancel(true)
            .addAction(
                R.mipmap.ic_launcher_round, "End Call",
                pIntent
            )

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }

        val timer = object : CountDownTimer(10 * 1000, 1000) {
            override fun onFinish() {
                builder.setProgress(0, 0, false)
                with(NotificationManagerCompat.from(this@MainActivity)) {
                    notify(NOTIFICATION_ID, builder.build())
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                builder.setProgress(100, (millisUntilFinished / 100).toInt(), false)
                with(NotificationManagerCompat.from(this@MainActivity)) {
                    notify(NOTIFICATION_ID, builder.build())
                }
            }
        }
        timer.start()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel",
                "channel_name",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "channel_desc"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

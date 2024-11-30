package ca.unb.mobiledev.plantpal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast


class AlarmReceiver : BroadcastReceiver() {

    //When this class is called, it tries to play the ringtone.
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return // Exit early if null

        val taskName = intent.getStringExtra("TASK_NAME") ?: "Unknown Task"
        try {
            val ringtone = RingtoneManager.getRingtone(
                context,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            )
            ringtone.play()

            Toast.makeText(context, "Alarm for task: $taskName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to play alarm for task: $taskName", Toast.LENGTH_SHORT).show()
        }
    }

}
